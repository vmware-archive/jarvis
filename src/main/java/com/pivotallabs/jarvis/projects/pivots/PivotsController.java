package com.pivotallabs.jarvis.projects.pivots;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class PivotsController {
    private PivotService pivotService;

    @Autowired
    public PivotsController(PivotService pivotService) {
        this.pivotService = pivotService;
    }

    @RequestMapping("/api/pivots")
    public Map<String, Object> findAllPivots() {
        return pivotService.findAllPivots();
    }
}
