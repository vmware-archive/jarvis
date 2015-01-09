package com.pivotallabs.jarvis.publictransit;

import com.pivotallabs.jarvis.publictransit.cta.JarvisEtaEntity;
import com.pivotallabs.jarvis.publictransit.cta.CtaTimeTableApiResponse;
import com.pivotallabs.jarvis.publictransit.cta.CtaPublicTransitService;
import com.pivotallabs.jarvis.util.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

public class PublicTransitServiceTest {
    private RestTemplate restTemplate;
    private CtaPublicTransitService publicTransitService;
    private MockRestServiceServer mockServer;

    @Before
    public void setup() {
        initMocks(this);

        restTemplate = new RestTemplate();
        mockServer = MockRestServiceServer.createServer(restTemplate);
        publicTransitService = new CtaPublicTransitService(restTemplate, "123456");
    }

    @Test
    public void getCtaTimeTable_ReturnsCtaTimeTableEntity() throws IOException {
        mockServer
            .expect(requestTo("http://lapi.transitchicago.com/api/1.0/ttarrivals.aspx?key=123456&stpid=30374,30375&max=100"))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withSuccess(FileUtils.readFile("cta.xml"), MediaType.APPLICATION_XML));
        mockServer
            .expect(requestTo("http://lapi.transitchicago.com/api/1.0/ttarrivals.aspx?key=123456&mapid=40330,40460&max=100"))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withSuccess(FileUtils.readFile("cta.xml"), MediaType.APPLICATION_XML));

        CtaTimeTableApiResponse entity = publicTransitService.getCtaTimeTable();

        mockServer.verify();

        assertThat(entity, not(nullValue()));
        assertThat(entity.getPredictions(), hasSize(4));

        JarvisEtaEntity firstCtaEtaEntity = entity.getPredictions().get(0);
        assertThat(firstCtaEtaEntity.getStation(), is("Merchandise Mart"));
        assertThat(firstCtaEtaEntity.getLine(), is("Brown"));
        assertThat(firstCtaEtaEntity.getDestination(), is("Kimball"));
        assertThat(firstCtaEtaEntity.getUpcomingTime(), is("2014-10-20T11:59:35"));

        JarvisEtaEntity secondCtaEtaEntity = entity.getPredictions().get(1);
        assertThat(secondCtaEtaEntity.getStation(), is("Merchandise Mart"));
        assertThat(secondCtaEtaEntity.getLine(), is("Brown"));
        assertThat(secondCtaEtaEntity.getDestination(), is("Kimball"));
        assertThat(secondCtaEtaEntity.getUpcomingTime(), is("2014-10-20T12:04:41"));
    }
}
