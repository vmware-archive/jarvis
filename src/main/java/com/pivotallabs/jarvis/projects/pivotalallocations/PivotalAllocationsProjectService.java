package com.pivotallabs.jarvis.projects.pivotalallocations;

import com.pivotallabs.jarvis.projects.*;
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
    public List<ProjectEntity> findAllProjects() {
        PivotalAllocationsApiResponse apiResponse = pivotalAllocationsClient.findAllAllocations();

        // get projects
        List<ProjectEntity> projects = new ArrayList<>();
        for (PivotalAllocationsProjectEntity pivotalAllocationsProjectEntity : apiResponse.getProjects()) {
            ProjectEntity projectEntity = new ProjectEntity();
            projectEntity.setId(pivotalAllocationsProjectEntity.getId());
            projectEntity.setName(pivotalAllocationsProjectEntity.getName());
            projects.add(projectEntity);
        }

        // get filtered employees
        List<EmployeeEntity> filteredEmployees = new ArrayList<>();
        for (PivotalAllocationsPersonEntity pivotalAllocationsPersonEntity : apiResponse.getPeople()) {
            if (apiResponse.getFilteredPeople().contains(pivotalAllocationsPersonEntity.getId())) {
                EmployeeEntity employeeEntity = new EmployeeEntity();
                employeeEntity.setId(pivotalAllocationsPersonEntity.getId());
                employeeEntity.setName(pivotalAllocationsPersonEntity.getName());
                filteredEmployees.add(employeeEntity);
            }
        }

        // get filtered allocations
        List<PivotalAllocationsAllocationEntity> filteredAllocations = new ArrayList<>();
        for (PivotalAllocationsAllocationEntity pivotalAllocationsAllocationEntity : apiResponse.getAllocations()) {
            if (apiResponse.getFilteredPeople().contains(pivotalAllocationsAllocationEntity.getPersonId())) {
                filteredAllocations.add(pivotalAllocationsAllocationEntity);
            }
        }

        // create new allocations
        for (PivotalAllocationsAllocationEntity pivotalAllocationsAllocationEntity : filteredAllocations) {
            ProjectEntity project = findProjectById(projects, pivotalAllocationsAllocationEntity.getProjectId());
            EmployeeEntity employee = findEmployeeById(filteredEmployees, pivotalAllocationsAllocationEntity.getPersonId());
            
            com.pivotallabs.jarvis.projects.AllocationEntity allocationEntity = new com.pivotallabs.jarvis.projects.AllocationEntity();
            allocationEntity.setEmployee(employee);
            allocationEntity.setStartDate(pivotalAllocationsAllocationEntity.getWeekStart());
            
            project.addAllocation(allocationEntity);
        }

        return projects;
    }

    private ProjectEntity findProjectById(List<ProjectEntity> projectEntities, int projectID) {
        for (ProjectEntity chicagoProject : projectEntities) {
            if (chicagoProject.getId() == projectID) {
                return chicagoProject;
            }
        }

        return null;
    }

    private EmployeeEntity findEmployeeById(List<EmployeeEntity> employeeEntities, int employeeID) {
        for (EmployeeEntity chicagoEmployeeEntity : employeeEntities) {
            if (chicagoEmployeeEntity.getId() == employeeID) {
                return chicagoEmployeeEntity;
            }
        }
        return null;
    }
}
