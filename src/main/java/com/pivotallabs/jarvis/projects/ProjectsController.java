package com.pivotallabs.jarvis.projects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProjectsController {
    private ProjectService projectService;

    @Autowired
    public ProjectsController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @RequestMapping(value = "/api/panels/allocations")
    public ProjectEntityWrapper listProjects() {
        List<ProjectEntity> projects = projectService.findAllProjects();
        return new ProjectEntityWrapper(projects);
    }

    public static class ProjectEntityWrapper {
        private List<ProjectEntity> projects;

        public ProjectEntityWrapper(List<ProjectEntity> projects) {
            this.projects = projects;
        }

        public List<ProjectEntity> getProjects() {
            return projects;
        }
    }
}
