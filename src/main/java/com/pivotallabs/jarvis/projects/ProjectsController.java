package com.pivotallabs.jarvis.projects;

import com.pivotallabs.jarvis.projects.domain.JarvisProjectEntity;
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

    @RequestMapping("/api/projects")
    public ProjectEntityWrapper listProjects() {
        List<JarvisProjectEntity> projects = projectService.findAllProjects();
        return new ProjectEntityWrapper(projects);
    }

    public static class ProjectEntityWrapper {
        private List<JarvisProjectEntity> projects;

        public ProjectEntityWrapper(List<JarvisProjectEntity> projects) {
            this.projects = projects;
        }

        public List<JarvisProjectEntity> getProjects() {
            return projects;
        }
    }
}
