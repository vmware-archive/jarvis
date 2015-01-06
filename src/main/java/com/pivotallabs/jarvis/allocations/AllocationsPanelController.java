package com.pivotallabs.jarvis.allocations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AllocationsPanelController {
    private AllocationsPanelDataProvider allocationsPanelDataProvider;

    @Autowired
    public AllocationsPanelController(AllocationsPanelDataProvider allocationsPanelDataProvider) {
        this.allocationsPanelDataProvider = allocationsPanelDataProvider;
    }

    @RequestMapping("/api/panels/allocations")
    public Object show() {
        return allocationsPanelDataProvider.loadPanelData();
    }
}
