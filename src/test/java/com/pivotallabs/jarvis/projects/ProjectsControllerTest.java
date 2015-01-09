package com.pivotallabs.jarvis.projects;

import com.pivotallabs.jarvis.projects.domain.JarvisProjectEntity;
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
    private ProjectService projectService;

    @InjectMocks
    private ProjectsController controller;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void listProjects_EndpointMapping() throws Exception {
        MockMvc mockMvc = standaloneSetup(controller).build();
        when(projectService.findAllProjects()).thenReturn(Arrays.asList(new JarvisProjectEntity()));

        mockMvc.perform(get("/api/projects"))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json;charset=UTF-8"));
    }

    @Test
    public void listProjects_ReturnsProjects() {
        JarvisProjectEntity expectedProject = new JarvisProjectEntity();
        when(projectService.findAllProjects()).thenReturn(Arrays.asList(expectedProject, new JarvisProjectEntity()));

        ProjectsController.ProjectEntityWrapper projectEntityWrapper = controller.listProjects();

        assertThat(projectEntityWrapper.getProjects(), hasSize(2));
        assertThat(projectEntityWrapper.getProjects().get(0), sameInstance(expectedProject));
    }
}