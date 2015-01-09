package com.pivotallabs.jarvis.projects.pivotalallocations;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pivotallabs.jarvis.projects.domain.JarvisAllocationEntity;
import com.pivotallabs.jarvis.projects.domain.JarvisPivotEntity;
import com.pivotallabs.jarvis.projects.domain.JarvisProjectEntity;

import java.util.List;

public class PivotalAllocationsApiResponse {
    @JsonProperty("filtered_people")
    private List<Integer> filteredPeople;
    @JsonProperty
    private List<JarvisAllocationEntity> allocations;
    @JsonProperty
    private List<JarvisProjectEntity> projects;
    @JsonProperty
    private List<JarvisPivotEntity> people;

    public void setFilteredPeople(List<Integer> filteredPeople) {
        this.filteredPeople = filteredPeople;
    }

    public List<Integer> getFilteredPeople() {
        return filteredPeople;
    }

    public void setAllocations(List<JarvisAllocationEntity> allocations) {
        this.allocations = allocations;
    }

    public List<JarvisAllocationEntity> getAllocations() {
        return allocations;
    }

    public void setProjects(List<JarvisProjectEntity> projects) {
        this.projects = projects;
    }

    public List<JarvisProjectEntity> getProjects() {
        return projects;
    }

    public void setPeople(List<JarvisPivotEntity> people) {
        this.people = people;
    }

    public List<JarvisPivotEntity> getPeople() {
        return people;
    }
}
