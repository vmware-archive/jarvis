package com.pivotallabs.jarvis.cta;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import util.FileUtils;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

public class CTAPanelDataProviderTest {
    private RestTemplate restTemplate;
    private CTAPanelDataProvider ctaPanelDataProvider;
    private MockRestServiceServer mockServer;

    @Before
    public void setup() {
        initMocks(this);

        restTemplate = new RestTemplate();
        mockServer = MockRestServiceServer.createServer(restTemplate);
        ctaPanelDataProvider = new CTAPanelDataProvider(restTemplate, "123456");
    }

    @Test
    public void loadPanelDataReturnsCTATimeTableEntity() throws IOException {
        mockServer
            .expect(requestTo("http://lapi.transitchicago.com/api/1.0/ttarrivals.aspx?key=123456&stpid=30374,30375&max=100"))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withSuccess(FileUtils.readFile("cta.xml"), MediaType.APPLICATION_XML));
        mockServer
            .expect(requestTo("http://lapi.transitchicago.com/api/1.0/ttarrivals.aspx?key=123456&mapid=40330,40460&max=100"))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withSuccess(FileUtils.readFile("cta.xml"), MediaType.APPLICATION_XML));

        CTATimeTableEntity entity = ctaPanelDataProvider.loadPanelData();

        mockServer.verify();

        assertThat(entity, not(nullValue()));
        assertThat(entity.getPredictions(), hasSize(4));

        CTAEtaEntity firstCtaEtaEntity = entity.getPredictions().get(0);
        assertThat(firstCtaEtaEntity.getStation(), is("Merchandise Mart"));
        assertThat(firstCtaEtaEntity.getLine(), is("Brown"));
        assertThat(firstCtaEtaEntity.getDestination(), is("Kimball"));
        assertThat(firstCtaEtaEntity.getUpcomingTime(), is("2014-10-20T11:59:35"));

        CTAEtaEntity secondCtaEtaEntity = entity.getPredictions().get(1);
        assertThat(secondCtaEtaEntity.getStation(), is("Merchandise Mart"));
        assertThat(secondCtaEtaEntity.getLine(), is("Brown"));
        assertThat(secondCtaEtaEntity.getDestination(), is("Kimball"));
        assertThat(secondCtaEtaEntity.getUpcomingTime(), is("2014-10-20T12:04:41"));
    }
}
