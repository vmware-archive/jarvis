package com.pivotallabs.jarvis.projects;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class ProjectsControllerTest {

    @Mock
    private ProjectService provider;

    @InjectMocks
    private ProjectsController controller;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void listProjects_EndpointMapping() throws Exception {
        MockMvc mockMvc = standaloneSetup(controller).build();
        when(provider.findAllProjects()).thenReturn(Arrays.asList(new ProjectEntity()));

        mockMvc.perform(get("/api/panels/projects"))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json;charset=UTF-8"));
    }

    @Test
    public void listProjects_ReturnsDataFromProvider() {
        ProjectEntity expectedProject = new ProjectEntity();
        when(provider.findAllProjects()).thenReturn(Arrays.asList(expectedProject, new ProjectEntity()));

        ProjectsController.ProjectEntityWrapper projectEntityWrapper = controller.listProjects();

        assertThat(projectEntityWrapper.getProjects(), hasSize(2));
        assertThat(projectEntityWrapper.getProjects().get(0), sameInstance(expectedProject));
    }
}