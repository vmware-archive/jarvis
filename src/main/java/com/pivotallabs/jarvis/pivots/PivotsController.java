package com.pivotallabs.jarvis.pivots;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PivotsController {
    private PivotService pivotService;

    @Autowired
    public PivotsController(PivotService pivotService) {
        this.pivotService = pivotService;
    }

    @RequestMapping("/api/pivots")
    public Object findAllPivots() {
        return pivotService.findAllPivots();
    }
}
