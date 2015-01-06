package com.pivotallabs.jarvis.cta;

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

public class CTAPanelControllerTest {

    @Mock
    CTAPanelDataProvider dataProvider;

    @InjectMocks
    CTAPanelController controller;

    @Before
    public void setup() {
        initMocks(this);
    }

    @Test
    public void showEndpointMapping() throws Exception {
        MockMvc mockMvc = standaloneSetup(controller).build();
        when(dataProvider.loadPanelData()).thenReturn(new CTATimeTableEntity());

        mockMvc.perform(get("/api/panels/cta"))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"));
    }

    @Test
    public void showReturnsCTATimeTableEntity() {
        CTATimeTableEntity expectedEntity = new CTATimeTableEntity();
        when(dataProvider.loadPanelData()).thenReturn(expectedEntity);

        CTATimeTableEntity actualEntity = controller.show();

        assertThat(actualEntity, is(expectedEntity));
    }
}
