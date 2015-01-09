package com.pivotallabs.jarvis.projects.pivotalallocations;

import com.pivotallabs.jarvis.projects.ProjectService;
import com.pivotallabs.jarvis.projects.domain.JarvisAllocationEntity;
import com.pivotallabs.jarvis.projects.domain.JarvisPivotEntity;
import com.pivotallabs.jarvis.projects.domain.JarvisProjectEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class PivotalAllocationsProjectService implements ProjectService {
    private PivotalAllocationsClient pivotalAllocationsClient;

    @Autowired
    public PivotalAllocationsProjectService(PivotalAllocationsClient pivotalAllocationsClient) {
        this.pivotalAllocationsClient = pivotalAllocationsClient;
    }

    @Override
    public List<JarvisProjectEntity> findAllProjects() {
        PivotalAllocationsApiResponse apiResponse = pivotalAllocationsClient.findAllAllocations();

        // get projects
        List<JarvisProjectEntity> projects = apiResponse.getProjects().stream()
            .map(project -> {
                JarvisProjectEntity projectEntity = new JarvisProjectEntity();
                projectEntity.setId(project.getId());
                projectEntity.setName(project.getName());
                return projectEntity;
            })
            .collect(toList());

        // get filtered pivots
        List<JarvisPivotEntity> filteredPivots = apiResponse.getPeople().stream()
            .filter(pivot -> apiResponse.getFilteredPeople().contains(pivot.getId()))
            .map(pivot -> {
                JarvisPivotEntity pivotEntity = new JarvisPivotEntity();
                pivotEntity.setId(pivot.getId());
                pivotEntity.setName(pivot.getName());
                return pivotEntity;
            })
            .collect(toList());

        // get filtered allocations
        List<JarvisAllocationEntity> filteredAllocations = apiResponse.getAllocations().stream()
            .filter(allocation -> apiResponse.getFilteredPeople().contains(allocation.getPersonId()))
            .collect(toList());

        // create new allocations
        filteredAllocations.stream()
            .forEach(allocation -> {
                JarvisProjectEntity project = findProjectById(projects, allocation.getProjectId());
                JarvisPivotEntity pivot = findPivotById(filteredPivots, allocation.getPersonId());

                JarvisAllocationEntity allocationEntity = new JarvisAllocationEntity();
                allocationEntity.setPivot(pivot);
                allocationEntity.setStartDate(allocation.getWeekStart());

                project.addAllocation(allocationEntity);
            });

        return projects;
    }

    private JarvisProjectEntity findProjectById(List<JarvisProjectEntity> projectEntities, int projectId) {
        return projectEntities.stream()
            .filter(chicagoProject -> chicagoProject.getId() == projectId)
            .findFirst()
            .get();
    }

    private JarvisPivotEntity findPivotById(List<JarvisPivotEntity> pivotEntities, int pivotId) {
        return pivotEntities.stream()
            .filter(chicagoPivotEntity -> chicagoPivotEntity.getId() == pivotId)
            .findFirst()
            .get();
    }
}
