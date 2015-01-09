package com.pivotallabs.jarvis.projects.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"personId", "projectId", "weekStart"})
public class JarvisAllocationEntity {
    private int personId;
    private int projectId;
    private String weekStart;

    private String startDate;
    private JarvisPivotEntity employee;

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getWeekStart() {
        return weekStart;
    }

    public void setWeekStart(String weekStart) {
        this.weekStart = weekStart;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public JarvisPivotEntity getEmployee() {
        return employee;
    }

    public void setEmployee(JarvisPivotEntity employee) {
        this.employee = employee;
    }
}
