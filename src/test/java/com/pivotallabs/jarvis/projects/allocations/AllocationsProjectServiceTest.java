package com.pivotallabs.jarvis.projects.allocations;

import com.pivotallabs.jarvis.projects.PivotEntity;
import com.pivotallabs.jarvis.projects.ProjectEntity;
import com.pivotallabs.jarvis.projects.ProjectService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.util.MatcherAssertionErrors.assertThat;

public class AllocationsProjectServiceTest {

    @Mock
    private AllocationsClient client;

    private ProjectService projectService;

    @Before
    public void setUp() {
        initMocks(this);

        projectService = new AllocationsProjectService(client);
    }

    @Test
    public void findAllProjects_translatesAllocationEntitiesIntoProjectEntities() {
        AllocationEntity awesomeProject = new AllocationEntity();
        awesomeProject.setProjectName("Awesome-O Project");
        AllocationEntity.AllocationPersonEntity johnB = new AllocationEntity.AllocationPersonEntity();
        johnB.setId(10);
        johnB.setName("John B");
        johnB.setEmail("jonny@example.com");
        awesomeProject.setPerson(johnB);

        AllocationEntity chicagoStatusBoardFirstAllocation = new AllocationEntity();
        chicagoStatusBoardFirstAllocation.setProjectName("Chicago Status Board");
        AllocationEntity.AllocationPersonEntity maryW = new AllocationEntity.AllocationPersonEntity();
        maryW.setId(15);
        maryW.setName("Mary Wilson");
        maryW.setEmail("mwilson@example.com");
        chicagoStatusBoardFirstAllocation.setPerson(maryW);

        AllocationEntity chicagoStatusBoardSecondAllocation = new AllocationEntity();
        chicagoStatusBoardSecondAllocation.setProjectName("Chicago Status Board");
        AllocationEntity.AllocationPersonEntity leonaF = new AllocationEntity.AllocationPersonEntity();
        leonaF.setId(20);
        leonaF.setName("Leona Farmer");
        leonaF.setEmail("lfarmer@example.com");
        chicagoStatusBoardSecondAllocation.setPerson(leonaF);

        when(client.findAllAllocations())
            .thenReturn(Arrays.asList(awesomeProject, chicagoStatusBoardFirstAllocation, chicagoStatusBoardSecondAllocation));

        List<ProjectEntity> projects = projectService.findAllProjects();

        assertThat(projects, hasSize(2));

        ProjectEntity firstProject = projects.get(0);
        assertThat(firstProject.getName(), is("Awesome-O Project"));
        PivotEntity firstProjectPivot = firstProject.getPivots().get(0);
        assertThat(firstProjectPivot.getId(), is(10));
        assertThat(firstProjectPivot.getName(), is("John B"));
        assertThat(firstProjectPivot.getEmail(), is("jonny@example.com"));

        ProjectEntity secondProject = projects.get(1);
        assertThat(secondProject.getName(), is("Chicago Status Board"));
        assertThat(secondProject.getPivots(), hasSize(2));
        PivotEntity secondProjectPivot = secondProject.getPivots().get(0);
        assertThat(secondProjectPivot.getId(), is(15));
        assertThat(secondProjectPivot.getName(), is("Mary Wilson"));
        assertThat(secondProjectPivot.getEmail(), is("mwilson@example.com"));
        PivotEntity thirdProjectPivot = secondProject.getPivots().get(1);
        assertThat(thirdProjectPivot.getId(), is(20));
        assertThat(thirdProjectPivot.getName(), is("Leona Farmer"));
        assertThat(thirdProjectPivot.getEmail(), is("lfarmer@example.com"));
    }
}