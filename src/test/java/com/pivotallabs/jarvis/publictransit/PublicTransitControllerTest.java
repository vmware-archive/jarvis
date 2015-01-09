package com.pivotallabs.jarvis.publictransit;

import com.pivotallabs.jarvis.publictransit.cta.CtaPublicTransitService;
import com.pivotallabs.jarvis.publictransit.cta.CtaTimeTableEntity;
import com.pivotallabs.jarvis.publictransit.divvy.DivvyService;
import com.pivotallabs.jarvis.publictransit.divvy.DivvyStationEntity;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class PublicTransitControllerTest {

    @Mock
    CtaPublicTransitService ctaService;
    
    @Mock
    DivvyService divvyService;

    @InjectMocks
    PublicTransitController controller;

    @Before
    public void setup() {
        initMocks(this);
    }

    @Test
    public void showCtaTimeTable_EndpointMapping() throws Exception {
        MockMvc mockMvc = standaloneSetup(controller).build();
        when(ctaService.getCtaTimeTable()).thenReturn(new CtaTimeTableEntity());

        mockMvc.perform(get("/api/public-transit/cta-timetable"))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"));
    }

    @Test
    public void showCtaTimeTable_ReturnsTheCtaTimeTable() {
        CtaTimeTableEntity expectedEntity = new CtaTimeTableEntity();
        when(ctaService.getCtaTimeTable()).thenReturn(expectedEntity);

        CtaTimeTableEntity actualEntity = controller.showCtaTimeTable();

        assertThat(actualEntity, is(expectedEntity));
    }

    @Test
    public void listDivvyStations_EndpointMapping() throws Exception {
        MockMvc mockMvc = standaloneSetup(controller).build();
        when(divvyService.findAllStations()).thenReturn(Arrays.asList(new DivvyStationEntity()));

        mockMvc.perform(get("/api/public-transit/divvy-stations"))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"));
    }
    
    @Test
    public void listDivvyStations_ReturnsStations() {
        List<DivvyStationEntity> expectedStations = Arrays.asList(new DivvyStationEntity(), new DivvyStationEntity());
        when(divvyService.findAllStations()).thenReturn(expectedStations);

        PublicTransitController.DivvyStationEntityWrapper response = controller.listDivvyStations();
        
        assertThat(response.getDivvyStations(), hasSize(2));
        assertThat(response.getDivvyStations(), sameInstance(expectedStations));
    }
}
