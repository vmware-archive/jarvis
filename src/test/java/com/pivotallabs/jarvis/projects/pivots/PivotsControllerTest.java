package com.pivotallabs.jarvis.projects.pivots;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class PivotsControllerTest {
    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        PivotService provider = new PivotService("/pivots.json");
        PivotsController controller = new PivotsController(provider);

        mockMvc = standaloneSetup(controller).build();
    }

    @Test
    public void getsPivotsData() throws Exception {
        mockMvc.perform(get("/api/pivots"))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.pivots", hasSize(1)))
            .andExpect(jsonPath("$.pivots[0].name", is("Gloria Coley")))
            .andExpect(jsonPath("$.pivots[0].email", is("gcoley@example.com")))
            .andExpect(jsonPath("$.pivots[0].phone", is("+1123456789")));
    }

}