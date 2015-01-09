package com.pivotallabs.jarvis.projects.pivotalallocations;

import com.pivotallabs.jarvis.projects.domain.JarvisAllocationEntity;
import com.pivotallabs.jarvis.projects.domain.JarvisPivotEntity;
import com.pivotallabs.jarvis.projects.domain.JarvisProjectEntity;
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
        JarvisProjectEntity firstAllocationsProject = new JarvisProjectEntity();
        firstAllocationsProject.setName("Chicago Status Board");
        firstAllocationsProject.setId(2);
        JarvisProjectEntity secondAllocationsProject = new JarvisProjectEntity();
        secondAllocationsProject.setName("Unallocated project");
        secondAllocationsProject.setId(5);
        apiResponse.setProjects(Arrays.asList(firstAllocationsProject, secondAllocationsProject));

        // allocations
        JarvisAllocationEntity firstAllocation = new JarvisAllocationEntity();
        firstAllocation.setPersonId(12345);
        firstAllocation.setProjectId(2);
        firstAllocation.setWeekStart("2015-01-05");
        JarvisAllocationEntity secondAllocation = new JarvisAllocationEntity();
        secondAllocation.setPersonId(23456);
        secondAllocation.setProjectId(2);
        secondAllocation.setWeekStart("2015-01-12");
        JarvisAllocationEntity thirdAllocation = new JarvisAllocationEntity();
        thirdAllocation.setPersonId(99999);
        apiResponse.setAllocations(Arrays.asList(firstAllocation, secondAllocation, thirdAllocation));

        // filtered_people
        apiResponse.setFilteredPeople(Arrays.asList(12345, 23456));

        // people
        JarvisPivotEntity firstPerson = new JarvisPivotEntity();
        firstPerson.setId(12345);
        firstPerson.setName("John B");
        JarvisPivotEntity secondPerson = new JarvisPivotEntity();
        secondPerson.setId(23456);
        secondPerson.setName("Leona Farmer");
        apiResponse.setPeople(Arrays.asList(firstPerson, secondPerson));

        when(client.findAllAllocations()).thenReturn(apiResponse);


        List<JarvisProjectEntity> projects = projectService.findAllProjects();

        assertThat(projects, hasSize(2));

        JarvisProjectEntity chicagoStatusBoard = projects.get(0);
        assertThat(chicagoStatusBoard.getId(), is(2));
        assertThat(chicagoStatusBoard.getName(), is("Chicago Status Board"));

        List<JarvisAllocationEntity> teamMembers = chicagoStatusBoard.getAllocations();
        assertThat(teamMembers, hasSize(2));

        JarvisAllocationEntity firstMember = teamMembers.get(0);
        assertThat(firstMember.getPivot().getId(), is(12345));
        assertThat(firstMember.getPivot().getName(), is("John B"));
        assertThat(firstMember.getStartDate(), is("2015-01-05"));

        JarvisAllocationEntity secondMember = teamMembers.get(1);
        assertThat(secondMember.getPivot().getId(), is(23456));
        assertThat(secondMember.getPivot().getName(), is("Leona Farmer"));
        assertThat(secondMember.getStartDate(), is("2015-01-12"));

        JarvisProjectEntity unallocatedProject = projects.get(1);
        assertThat(unallocatedProject.getId(), is(5));
        assertThat(unallocatedProject.getName(), is("Unallocated project"));
        assertThat(unallocatedProject.getAllocations(), is(empty()));
    }
}