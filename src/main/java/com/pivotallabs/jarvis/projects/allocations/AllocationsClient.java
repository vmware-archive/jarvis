package com.pivotallabs.jarvis.projects.allocations;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AllocationsClient {
    private static final String ALLOCATIONS_API_URL = "https://allocations.cfapps.io/api/person_current_allocation.json";

    private final RestTemplate restTemplate;
    private final String apiKey;
    private final int accountId;
    private final String[] emails;

    @Autowired
    public AllocationsClient(
        RestTemplate restTemplate,
        @Value("${allocations.apiKey}") String apiKey,
        @Value("${allocations.accountID}") int accountId,
        @Value("${allocations.emails}") String emails
    ) {
        this.restTemplate = restTemplate;
        this.apiKey = apiKey;
        this.accountId = accountId;
        this.emails = emails.split("\\s*,\\s*");
    }

    public List<AllocationEntity> findAllAllocations() {
        ResponseEntity<List<AllocationEntity>> entity = restTemplate.exchange(
            "{api}?data={queryData}",
            HttpMethod.GET,
            HttpEntity.EMPTY,
            new ParameterizedTypeReference<List<AllocationEntity>>() {
            },
            ALLOCATIONS_API_URL,
            queryData()
        );

        return entity.getBody();
    }

    private String queryData() {
        Map<String, Object> map = new HashMap<>();
        map.put("api_token", apiKey);
        map.put("account_id", accountId);
        map.put("emails", emails);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(map);
        } catch (IOException e) {
            return "";
        }
    }
}
