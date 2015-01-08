package com.pivotallabs.jarvis.projects.pivotalallocations;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class PivotalAllocationsApiResponse {
    @JsonProperty("filtered_people")
    private List<Integer> filteredPeople;
    private List<PivotalAllocationsAllocationEntity> allocations;
    private List<PivotalAllocationsProjectEntity> projects;
    private List<PivotalAllocationsPersonEntity> people;

    public void setFilteredPeople(List<Integer> filteredPeople) {
        this.filteredPeople = filteredPeople;
    }

    public List<Integer> getFilteredPeople() {
        return filteredPeople;
    }

    public void setAllocations(List<PivotalAllocationsAllocationEntity> allocations) {
        this.allocations = allocations;
    }

    public List<PivotalAllocationsAllocationEntity> getAllocations() {
        return allocations;
    }

    public void setProjects(List<PivotalAllocationsProjectEntity> projects) {
        this.projects = projects;
    }

    public List<PivotalAllocationsProjectEntity> getProjects() {
        return projects;
    }

    public void setPeople(List<PivotalAllocationsPersonEntity> people) {
        this.people = people;
    }

    public List<PivotalAllocationsPersonEntity> getPeople() {
        return people;
    }
}
