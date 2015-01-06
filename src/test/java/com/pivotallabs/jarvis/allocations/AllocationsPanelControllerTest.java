package com.pivotallabs.jarvis.allocations;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.BasicHttpEntity;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;

import java.io.InputStream;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class AllocationsPanelControllerTest {

    private MockMvc mockMvc;

    @Mock
    private HttpClient mockHttpClient;

    @Mock
    private HttpResponse mockHttpResponse;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        AllocationsPanelDataProvider provider = new AllocationsPanelDataProvider(mockHttpClient, "apiKey", 0, "emails, emails");
        AllocationsPanelController controller = new AllocationsPanelController(provider);

        mockMvc = standaloneSetup(controller).build();
    }

    @Test
    public void getsAllocationsData() throws Exception {
        InputStream allocationsInputStream = getClass().getResourceAsStream("/allocations.json");

        BasicHttpEntity httpEntity = new BasicHttpEntity();
        httpEntity.setContent(allocationsInputStream);

        Mockito.when(mockHttpClient.execute(Mockito.any())).thenReturn(mockHttpResponse);
        Mockito.when(mockHttpResponse.getEntity()).thenReturn(httpEntity);

        mockMvc.perform(get("/api/panels/allocations"))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.projects", hasSize(2)))
            .andExpect(jsonPath("$.projects[0].name", is("Insurance Solutions App")))
            .andExpect(jsonPath("$.projects[0].pivotsByWeek", hasSize(1)))
            .andExpect(jsonPath("$.projects[0].pivotsByWeek[0]", hasSize(1)))
            .andExpect(jsonPath("$.projects[0].pivotsByWeek[0][0].name", is("Everett Nelson")))
            .andExpect(jsonPath("$.projects[1].name", is("Chicago Status Board")))
            .andExpect(jsonPath("$.projects[1].pivotsByWeek", hasSize(1)))
            .andExpect(jsonPath("$.projects[1].pivotsByWeek[0]", hasSize(2)))
            .andExpect(jsonPath("$.projects[1].pivotsByWeek[0][0].name", is("Mary Wilson")))
            .andExpect(jsonPath("$.projects[1].pivotsByWeek[0][1].name", is("Leona Farmer")));
    }
}