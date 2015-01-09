package com.pivotallabs.jarvis.projects.domain;

import java.util.ArrayList;
import java.util.List;

public class JarvisProjectEntity {
    private int id;
    private String name;
    private List<JarvisAllocationEntity> allocations = new ArrayList<>();

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

    public List<JarvisAllocationEntity> getAllocations() {
        return allocations;
    }

    public void setAllocations(List<JarvisAllocationEntity> allocations) {
        this.allocations = allocations;
    }

    public void addAllocation(JarvisAllocationEntity allocationEntity) {
        this.allocations.add(allocationEntity);
    }
}
