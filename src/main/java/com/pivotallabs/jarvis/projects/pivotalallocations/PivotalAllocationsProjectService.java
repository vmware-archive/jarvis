package com.pivotallabs.jarvis.projects.pivotalallocations;

import com.pivotallabs.jarvis.projects.domain.JarvisAllocationEntity;
import com.pivotallabs.jarvis.projects.domain.JarvisPivotEntity;
import com.pivotallabs.jarvis.projects.domain.JarvisProjectEntity;
import com.pivotallabs.jarvis.projects.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
        List<JarvisProjectEntity> projects = new ArrayList<>();
        for (JarvisProjectEntity pivotalAllocationsProjectEntity : apiResponse.getProjects()) {
            JarvisProjectEntity project = new JarvisProjectEntity();
            project.setId(pivotalAllocationsProjectEntity.getId());
            project.setName(pivotalAllocationsProjectEntity.getName());
            projects.add(project);
        }

        // get filtered employees
        List<JarvisPivotEntity> filteredEmployees = new ArrayList<>();
        for (JarvisPivotEntity employee : apiResponse.getPeople()) {
            if (apiResponse.getFilteredPeople().contains(employee.getId())) {
                JarvisPivotEntity employeeEntity = new JarvisPivotEntity();
                employeeEntity.setId(employee.getId());
                employeeEntity.setName(employee.getName());
                filteredEmployees.add(employeeEntity);
            }
        }

        // get filtered allocations
        List<JarvisAllocationEntity> filteredAllocations = new ArrayList<>();
        for (JarvisAllocationEntity allocation : apiResponse.getAllocations()) {
            if (apiResponse.getFilteredPeople().contains(allocation.getPersonId())) {
                filteredAllocations.add(allocation);
            }
        }

        // create new allocations
        for (JarvisAllocationEntity allocation : filteredAllocations) {
            JarvisProjectEntity project = findProjectById(projects, allocation.getProjectId());
            JarvisPivotEntity employee = findEmployeeById(filteredEmployees, allocation.getPersonId());

            JarvisAllocationEntity allocationEntity = new JarvisAllocationEntity();
            allocationEntity.setEmployee(employee);
            allocationEntity.setStartDate(allocation.getWeekStart());

            project.addAllocation(allocationEntity);
        }

        return projects;
    }

    private JarvisProjectEntity findProjectById(List<JarvisProjectEntity> projectEntities, int projectId) {
        for (JarvisProjectEntity chicagoProject : projectEntities) {
            if (chicagoProject.getId() == projectId) {
                return chicagoProject;
            }
        }

        return null;
    }

    private JarvisPivotEntity findEmployeeById(List<JarvisPivotEntity> employeeEntities, int employeeId) {
        for (JarvisPivotEntity chicagoEmployeeEntity : employeeEntities) {
            if (chicagoEmployeeEntity.getId() == employeeId) {
                return chicagoEmployeeEntity;
            }
        }
        return null;
    }
}
