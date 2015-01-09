package com.pivotallabs.jarvis.publictransit.divvy;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import util.FileUtils;
import util.TestUtils;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

public class DivvyServiceTest {
    private MockRestServiceServer mockServer;
    private DivvyService divvyService;

    @Before
    public void setUp() {
        RestTemplate restTemplate = TestUtils.testRestTemplate();
        mockServer = MockRestServiceServer.createServer(restTemplate);
        divvyService = new DivvyService(restTemplate, "5, 14");
    }

    @Test
    public void findAllStations_ReturnsDesiredDivvyStations() throws IOException {
        mockServer
            .expect(requestTo("http://www.divvybikes.com/stations/json"))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withSuccess(FileUtils.readFile("divvyStationsResponse.json"), MediaType.APPLICATION_JSON));

        List<DivvyStationEntity> stations = divvyService.findAllStations();

        mockServer.verify();

        assertThat(stations, hasSize(2));

        DivvyStationEntity firstStation = stations.get(0);
        assertThat(firstStation.getId(), is(5));
        assertThat(firstStation.getStationName(), is("State St & Harrison St"));
        assertThat(firstStation.getAvailableBikes(), is(12));
        assertThat(firstStation.getAvailableDocks(), is(7));

        DivvyStationEntity secondStation = stations.get(1);
        assertThat(secondStation.getId(), is(14));
        assertThat(secondStation.getStationName(), is("Morgan St & 18th St"));
        assertThat(secondStation.getAvailableBikes(), is(3));
        assertThat(secondStation.getAvailableDocks(), is(12));
    }

    @Test(expected = IllegalArgumentException.class)
    public void creatingADivvyServiceWithNullNearbyDivvyStationIdsThrowsAnException() {
        new DivvyService(TestUtils.testRestTemplate(), null);
    }
}