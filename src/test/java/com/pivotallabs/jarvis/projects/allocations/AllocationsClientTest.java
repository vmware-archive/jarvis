package com.pivotallabs.jarvis.projects.allocations;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import util.FileUtils;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.util.MatcherAssertionErrors.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

public class AllocationsClientTest {

    private MockRestServiceServer mockServer;
    private AllocationsClient allocationsClient;

    @Before
    public void setUp() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(0, mappingJacksonHttpMessageConverter());
        mockServer = MockRestServiceServer.createServer(restTemplate);
        allocationsClient = new AllocationsClient(restTemplate, "apiKey", 99, "emails, emails, emails");
    }

    @Test
    public void findAllAllocations_ReturnsAllocationsForItsEmails() throws IOException {
        mockServer
            .expect(requestTo("https://allocations.cfapps.io/api/person_current_allocation.json?data=%7B%22emails%22:%5B%22emails%22,%22emails%22,%22emails%22%5D,%22account_id%22:99,%22api_token%22:%22apiKey%22%7D"))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withSuccess(FileUtils.readFile("allocations.json"), MediaType.APPLICATION_JSON));

        List<AllocationEntity> allocations = allocationsClient.findAllAllocations();

        mockServer.verify();

        assertThat(allocations, hasSize(3));
        AllocationEntity firstAllocation = allocations.get(0);
        assertThat(firstAllocation.getProjectName(), is("Insurance Solutions App"));
        assertThat(firstAllocation.getPerson().getName(), is("Everett Nelson"));
        assertThat(firstAllocation.getPerson().getEmail(), is("enelson@example.com"));
        assertThat(firstAllocation.getPerson().getId(), is(357298));
    }

    private MappingJackson2HttpMessageConverter mappingJacksonHttpMessageConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        converter.setObjectMapper(objectMapper);

        return converter;
    }
}