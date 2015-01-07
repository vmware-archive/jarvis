package com.pivotallabs.jarvis.projects.allocations;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AllocationEntity {

    @JsonProperty("project_name")
    private String projectName;

    private AllocationPersonEntity person;

    public AllocationPersonEntity getPerson() {
        return person;
    }

    public void setPerson(AllocationPersonEntity person) {
        this.person = person;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public static class AllocationPersonEntity {
        private int id;
        private String name;
        private String email;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
