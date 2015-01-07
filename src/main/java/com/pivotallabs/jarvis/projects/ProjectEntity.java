package com.pivotallabs.jarvis.projects;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class ProjectEntity {

    private String name;

    @JsonProperty("pivotsByWeek")
    private List<PivotEntity> pivots = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PivotEntity> getPivots() {
        return pivots;
    }

    public void setPivots(List<PivotEntity> pivots) {
        this.pivots = pivots;
    }
    
    public void addPivot(PivotEntity pivot) {
        this.pivots.add(pivot);
    }
}
