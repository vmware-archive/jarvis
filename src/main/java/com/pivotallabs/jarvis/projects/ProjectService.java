package com.pivotallabs.jarvis.projects;

import com.pivotallabs.jarvis.projects.domain.JarvisProjectEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProjectService {
    List<JarvisProjectEntity> findAllProjects();
}
