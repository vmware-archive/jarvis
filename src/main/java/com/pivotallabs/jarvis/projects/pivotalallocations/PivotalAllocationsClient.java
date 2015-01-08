package com.pivotallabs.jarvis.projects.pivotalallocations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PivotalAllocationsClient {
    private static final String ALLOCATIONS_API_URL = "https://allocations.cfapps.io/accounts/3/matrix?week_offset=0&week_count=2&location_ids[]=35";

    private final RestTemplate restTemplate;
    private final String cookieValue;

    @Autowired
    public PivotalAllocationsClient(RestTemplate restTemplate, @Value("${pivotalallocations.cookie}") String cookieValue) {
        this.restTemplate = restTemplate;
        this.cookieValue = cookieValue;
    }

    public PivotalAllocationsApiResponse findAllAllocations() {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Cookie", cookieValue);
        HttpEntity requestEntity = new HttpEntity(null, requestHeaders);

        ResponseEntity<PivotalAllocationsApiResponse> responseEntity = restTemplate.exchange(
            ALLOCATIONS_API_URL,
            HttpMethod.GET,
            requestEntity,
            PivotalAllocationsApiResponse.class);

        return responseEntity.getBody();
    }
}
