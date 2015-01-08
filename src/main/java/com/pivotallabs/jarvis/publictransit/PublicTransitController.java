package com.pivotallabs.jarvis.publictransit;

import com.pivotallabs.jarvis.publictransit.cta.CtaPublicTransitService;
import com.pivotallabs.jarvis.publictransit.cta.CtaTimeTableEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PublicTransitController {
    private CtaPublicTransitService publicTransitService;

    @Autowired
    public PublicTransitController(CtaPublicTransitService publicTransitService) {
        this.publicTransitService = publicTransitService;
    }

    @RequestMapping(value = "/api/public-transit/cta-timetable", produces = MediaType.APPLICATION_JSON_VALUE)
    public CtaTimeTableEntity show() {
        return publicTransitService.loadPanelData();
    }
}
