package com.pivotallabs.jarvis.projects.pivotalallocations;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PivotalAllocationsAllocationEntity {
    @JsonProperty("person_id")
    private int personId;
    @JsonProperty("project_id")
    private int projectId;
    @JsonProperty("week_start")
    private String weekStart;

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public int getPersonId() {
        return personId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setWeekStart(String weekStart) {
        this.weekStart = weekStart;
    }

    public String getWeekStart() {
        return weekStart;
    }
}
