package com.pivotallabs.jarvis.publictransit;

import com.pivotallabs.jarvis.publictransit.cta.CtaTimeTableEntity;
import com.pivotallabs.jarvis.publictransit.cta.CtaPublicTransitService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class PublicTransitControllerTest {

    @Mock
    CtaPublicTransitService dataProvider;

    @InjectMocks
    PublicTransitController controller;

    @Before
    public void setup() {
        initMocks(this);
    }

    @Test
    public void showEndpointMapping() throws Exception {
        MockMvc mockMvc = standaloneSetup(controller).build();
        when(dataProvider.loadPanelData()).thenReturn(new CtaTimeTableEntity());

        mockMvc.perform(get("/api/public-transit/cta-timetable"))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"));
    }

    @Test
    public void showReturnsCTATimeTableEntity() {
        CtaTimeTableEntity expectedEntity = new CtaTimeTableEntity();
        when(dataProvider.loadPanelData()).thenReturn(expectedEntity);

        CtaTimeTableEntity actualEntity = controller.show();

        assertThat(actualEntity, is(expectedEntity));
    }
}
