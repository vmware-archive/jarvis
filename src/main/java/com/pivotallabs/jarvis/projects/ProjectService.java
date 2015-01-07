package com.pivotallabs.jarvis.projects;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProjectService {
    List<ProjectEntity> findAllProjects();
}
