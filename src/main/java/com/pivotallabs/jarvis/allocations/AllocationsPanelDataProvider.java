package com.pivotallabs.jarvis.allocations;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class AllocationsPanelDataProvider {
    private HttpClient httpClient;
    private String allocationsApiKey;
    private int allocationsAccountID;
    private String allocationsEmails;
    private Pattern splitPattern;

    private Object cachedJSONResult = null;

    @Autowired
    public AllocationsPanelDataProvider(
        HttpClient httpClient,
        @Value("${allocations.apiKey}") String allocationsApiKey,
        @Value("${allocations.accountID}") int allocationsAccountID,
        @Value("${allocations.emails}") String allocationsEmails
    ) {
        this.httpClient = httpClient;
        this.allocationsApiKey = allocationsApiKey;
        this.allocationsAccountID = allocationsAccountID;
        this.allocationsEmails = allocationsEmails;
        this.splitPattern = Pattern.compile("\\s*,\\s*");
    }

    public Object loadPanelData() {
        synchronized (this) {
            if (cachedJSONResult != null) {
                System.out.println("Returning cached data");
                return cachedJSONResult;
            }
        }

        String[] emails = splitPattern.split(allocationsEmails);
        StringJoiner sj = new StringJoiner("\",\"", "\"", "\"");
        for (int i = 0; i < emails.length; i++) {
            sj.add(emails[i]);
        }

        String queryData = String.format(
            "{\"api_token\":\"%s\", \"account_id\":%d, \"emails\":[%s]}", allocationsApiKey, allocationsAccountID, sj.toString());
        String encodedQueryData = URLEncodedUtils.format(Collections.singletonList(new BasicNameValuePair("data", queryData)), "UTF-8");
        HttpUriRequest request = new HttpGet("https://allocations.cfapps.io/api/person_current_allocation.json?" + encodedQueryData);

        try {
            HttpResponse httpResponse = this.httpClient.execute(request);
            try (InputStream contentStream = httpResponse.getEntity().getContent()) {
                BufferedReader bufferedContentReader = new BufferedReader(new InputStreamReader(contentStream));
                StringBuilder buffer = new StringBuilder();

                String jsonLine;
                while ((jsonLine = bufferedContentReader.readLine()) != null) {
                    buffer.append(jsonLine);
                }

                JacksonJsonParser jsonParser = new JacksonJsonParser();

                Map<String, Object> jsonRootObject = new HashMap<>();
                List<Object> jsonList = jsonParser.parseList(buffer.toString());
                List<Object> projectsList = new LinkedList<>();
                jsonRootObject.put("projects", projectsList);

                Map<String, List<Map<String, String>>> projectMap = new HashMap<>();
                for (Object jsonPivot : jsonList) {
                    assert (jsonPivot instanceof Map);
                    Map<String, Object> jsonPivotMap = (Map<String, Object>) jsonPivot;
                    String projectName = (String) jsonPivotMap.get("project_name");

                    List<Map<String, String>> pivots = projectMap.get(projectName);
                    if (pivots == null) {
                        pivots = new LinkedList<>();
                        projectMap.put(projectName, pivots);
                    }

                    pivots.add((Map) jsonPivotMap.get("person"));
                }

                for (Map.Entry<String, List<Map<String, String>>> projectMapEntry : projectMap.entrySet()) {
                    Map<String, Object> jsonProject = new HashMap<>();
                    jsonProject.put("name", projectMapEntry.getKey());
                    jsonProject.put("pivotsByWeek", Collections.singletonList(projectMapEntry.getValue()));
                    projectsList.add(jsonProject);
                }

                cachedJSONResult = jsonRootObject;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return cachedJSONResult;
    }
}
