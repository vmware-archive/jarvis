package com.pivotallabs.jarvis.projects.pivots;

import com.pivotallabs.jarvis.projects.domain.JarvisPivotEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class PivotsController {
    private PivotService pivotService;

    @Autowired
    public PivotsController(PivotService pivotService) {
        this.pivotService = pivotService;
    }

    @RequestMapping("/api/pivots")
    public ListPivotsWrapper findAllPivots() {
        return new ListPivotsWrapper(pivotService.findAllPivots());
    }

    public static class ListPivotsWrapper {
        List<JarvisPivotEntity> pivots;

        public ListPivotsWrapper(List<JarvisPivotEntity> pivots) {
            this.pivots = pivots;
        }

        public List<JarvisPivotEntity> getPivots() {
            return pivots;
        }

        public void setPivots(List<JarvisPivotEntity> pivots) {
            this.pivots = pivots;
        }
    }
}
