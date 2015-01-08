package com.pivotallabs.jarvis.projects;

import java.util.ArrayList;
import java.util.List;

public class ProjectEntity {
    private int id;
    private String name;
    private List<AllocationEntity> allocations = new ArrayList<>();

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

    public List<AllocationEntity> getAllocations() {
        return allocations;
    }

    public void setAllocations(List<AllocationEntity> allocations) {
        this.allocations = allocations;
    }

    public void addAllocation(AllocationEntity allocationEntity) {
        this.allocations.add(allocationEntity);
    }
}
