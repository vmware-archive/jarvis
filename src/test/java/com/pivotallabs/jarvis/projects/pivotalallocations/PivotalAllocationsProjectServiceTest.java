package com.pivotallabs.jarvis.projects.pivotalallocations;

import com.pivotallabs.jarvis.projects.AllocationEntity;
import com.pivotallabs.jarvis.projects.ProjectEntity;
import com.pivotallabs.jarvis.projects.ProjectService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.util.MatcherAssertionErrors.assertThat;

public class PivotalAllocationsProjectServiceTest {

    @Mock
    private PivotalAllocationsClient client;

    private ProjectService projectService;

    @Before
    public void setUp() {
        initMocks(this);

        projectService = new PivotalAllocationsProjectService(client);
    }

    @Test
    public void findAllProjects_translatesPivotalAllocationsClientResponse() {
        PivotalAllocationsApiResponse apiResponse = new PivotalAllocationsApiResponse();

        // projects
        PivotalAllocationsProjectEntity firstAllocationsProject = new PivotalAllocationsProjectEntity();
        firstAllocationsProject.setName("Chicago Status Board");
        firstAllocationsProject.setId(2);
        PivotalAllocationsProjectEntity secondAllocationsProject = new PivotalAllocationsProjectEntity();
        secondAllocationsProject.setName("Unallocated project");
        secondAllocationsProject.setId(5);
        apiResponse.setProjects(Arrays.asList(firstAllocationsProject, secondAllocationsProject));

        // allocations
        PivotalAllocationsAllocationEntity firstAllocation = new PivotalAllocationsAllocationEntity();
        firstAllocation.setPersonId(12345);
        firstAllocation.setProjectId(2);
        firstAllocation.setWeekStart("2015-01-05");
        PivotalAllocationsAllocationEntity secondAllocation = new PivotalAllocationsAllocationEntity();
        secondAllocation.setPersonId(23456);
        secondAllocation.setProjectId(2);
        secondAllocation.setWeekStart("2015-01-12");
        PivotalAllocationsAllocationEntity thirdAllocation = new PivotalAllocationsAllocationEntity();
        thirdAllocation.setPersonId(99999);
        apiResponse.setAllocations(Arrays.asList(firstAllocation, secondAllocation, thirdAllocation));

        // filtered_people
        apiResponse.setFilteredPeople(Arrays.asList(12345, 23456));

        // people
        PivotalAllocationsPersonEntity firstPerson = new PivotalAllocationsPersonEntity();
        firstPerson.setId(12345);
        firstPerson.setName("John B");
        PivotalAllocationsPersonEntity secondPerson = new PivotalAllocationsPersonEntity();
        secondPerson.setId(23456);
        secondPerson.setName("Leona Farmer");
        apiResponse.setPeople(Arrays.asList(firstPerson, secondPerson));

        when(client.findAllAllocations()).thenReturn(apiResponse);


        List<ProjectEntity> projects = projectService.findAllProjects();

        assertThat(projects, hasSize(2));

        ProjectEntity chicagoStatusBoard = projects.get(0);
        assertThat(chicagoStatusBoard.getId(), is(2));
        assertThat(chicagoStatusBoard.getName(), is("Chicago Status Board"));

        List<AllocationEntity> teamMembers = chicagoStatusBoard.getAllocations();
        assertThat(teamMembers, hasSize(2));

        AllocationEntity firstMember = teamMembers.get(0);
        assertThat(firstMember.getEmployee().getId(), is(12345));
        assertThat(firstMember.getEmployee().getName(), is("John B"));
        assertThat(firstMember.getStartDate(), is("2015-01-05"));

        AllocationEntity secondMember = teamMembers.get(1);
        assertThat(secondMember.getEmployee().getId(), is(23456));
        assertThat(secondMember.getEmployee().getName(), is("Leona Farmer"));
        assertThat(secondMember.getStartDate(), is("2015-01-12"));

        ProjectEntity unallocatedProject = projects.get(1);
        assertThat(unallocatedProject.getId(), is(5));
        assertThat(unallocatedProject.getName(), is("Unallocated project"));
        assertThat(unallocatedProject.getAllocations(), is(empty()));
    }
}