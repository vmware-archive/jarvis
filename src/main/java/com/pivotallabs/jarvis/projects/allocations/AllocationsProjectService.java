package com.pivotallabs.jarvis.projects.allocations;

import com.pivotallabs.jarvis.projects.PivotEntity;
import com.pivotallabs.jarvis.projects.ProjectEntity;
import com.pivotallabs.jarvis.projects.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AllocationsProjectService implements ProjectService {
    private AllocationsClient allocationsClient;

    @Autowired
    public AllocationsProjectService(AllocationsClient allocationsClient) {
        this.allocationsClient = allocationsClient;
    }

    @Override
    public List<ProjectEntity> findAllProjects() {
        List<ProjectEntity> projectEntities = new ArrayList<>();
        for (AllocationEntity allocation : allocationsClient.findAllAllocations()) {
            ProjectEntity projectEntity = findOrCreateProjectEntityByName(projectEntities, allocation.getProjectName());

            AllocationEntity.AllocationPersonEntity person = allocation.getPerson();

            PivotEntity pivot = new PivotEntity();
            pivot.setId(person.getId());
            pivot.setName(person.getName());
            pivot.setEmail(person.getEmail());

            projectEntity.addPivot(pivot);

        }

        return projectEntities;
    }

    private ProjectEntity findOrCreateProjectEntityByName(List<ProjectEntity> projectEntities, String projectName) {
        for (ProjectEntity existingProjectEntity : projectEntities) {
            if (existingProjectEntity.getName().equals(projectName)) {
                return existingProjectEntity;
            }
        }

        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setName(projectName);
        projectEntities.add(projectEntity);
        return projectEntity;
    }
}
