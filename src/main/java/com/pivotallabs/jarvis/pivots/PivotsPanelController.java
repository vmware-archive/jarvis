package com.pivotallabs.jarvis.pivots;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PivotsPanelController {
    private PivotsPanelDataProvider pivotsPanelDataProvider;

    @Autowired
    public PivotsPanelController(PivotsPanelDataProvider pivotsPanelDataProvider) {
        this.pivotsPanelDataProvider = pivotsPanelDataProvider;
    }

    @RequestMapping("/api/panels/pivots")
    public Object show() {
        return pivotsPanelDataProvider.loadPanelData();
    }
}
