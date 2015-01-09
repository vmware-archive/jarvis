package com.pivotallabs.jarvis.projects.pivotalallocations;

import com.pivotallabs.jarvis.util.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import util.TestUtils;

import java.io.IOException;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.util.MatcherAssertionErrors.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

public class PivotalAllocationsClientTest {

    private MockRestServiceServer mockServer;
    private PivotalAllocationsClient client;

    @Before
    public void setUp() {
        RestTemplate restTemplate = TestUtils.restTemplate();
        mockServer = MockRestServiceServer.createServer(restTemplate);
        client = new PivotalAllocationsClient(restTemplate, "cookie-value");
    }

    @Test
    public void findAllAllocations() throws IOException {
        mockServer
            .expect(requestTo("https://allocations.cfapps.io/accounts/3/matrix?week_offset=0&week_count=2&location_ids%5B%5D=35"))
            .andExpect(method(HttpMethod.GET))
            .andExpect(header("Cookie", "cookie-value"))
            .andRespond(withSuccess(FileUtils.readFile("pivotalAllocationsResponse.json"), MediaType.APPLICATION_JSON));

        PivotalAllocationsApiResponse apiResponse = client.findAllAllocations();

        // filtered_people
        assertThat(apiResponse.getFilteredPeople(), containsInAnyOrder(1, 2, 3));

        // projects
        assertThat(apiResponse.getProjects(), hasSize(3));
        assertThat(apiResponse.getProjects().get(0).getId(), is(1));
        assertThat(apiResponse.getProjects().get(0).getName(), is("Hackathon"));
        assertThat(apiResponse.getProjects().get(2).getId(), is(-1));
        assertThat(apiResponse.getProjects().get(2).getName(), is("Unallocated"));

        // allocations
        assertThat(apiResponse.getAllocations(), hasSize(4));
        assertThat(apiResponse.getAllocations().get(0).getPersonId(), is(1));
        assertThat(apiResponse.getAllocations().get(0).getProjectId(), is(1));
        assertThat(apiResponse.getAllocations().get(0).getWeekStart(), is("2015-01-05"));
        assertThat(apiResponse.getAllocations().get(3).getPersonId(), is(1));
        assertThat(apiResponse.getAllocations().get(3).getProjectId(), is(1));
        assertThat(apiResponse.getAllocations().get(3).getWeekStart(), is("2015-01-12"));

        // people
        assertThat(apiResponse.getPeople(), hasSize(3));
        assertThat(apiResponse.getPeople().get(0).getId(), is(1));
        assertThat(apiResponse.getPeople().get(0).getName(), is("Bob Smith"));
        assertThat(apiResponse.getPeople().get(2).getId(), is(3));
        assertThat(apiResponse.getPeople().get(2).getName(), is("Mary Sue"));

    }
}