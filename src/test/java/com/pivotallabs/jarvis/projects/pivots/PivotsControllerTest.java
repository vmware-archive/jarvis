package com.pivotallabs.jarvis.projects.pivots;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class PivotsControllerTest {
    private MockMvc mockMvc;

    @Mock
    private PivotService pivotService;

    private PivotsController controller;

    @Before
    public void setUp() {
        initMocks(this);

        controller = new PivotsController(pivotService);

        mockMvc = standaloneSetup(controller).build();
    }

    @Test
    public void findAllPivots_EndpointMapping() throws Exception {
        when(pivotService.findAllPivots()).thenReturn(new HashMap<>());

        mockMvc.perform(get("/api/pivots"))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json;charset=UTF-8"));
    }

    @Test
    public void findAllPivots_ReturnsPivots() throws Exception {
        Map<String, Object> expectedPivots = new HashMap<>();
        when(pivotService.findAllPivots()).thenReturn(expectedPivots);

        Object actualPivots = controller.findAllPivots();

        Assert.assertThat(expectedPivots, is(sameInstance(actualPivots)));
    }
}