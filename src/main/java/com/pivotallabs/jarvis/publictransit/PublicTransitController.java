package com.pivotallabs.jarvis.publictransit;

import com.pivotallabs.jarvis.publictransit.cta.CtaPublicTransitService;
import com.pivotallabs.jarvis.publictransit.cta.CtaTimeTableEntity;
import com.pivotallabs.jarvis.publictransit.divvy.DivvyService;
import com.pivotallabs.jarvis.publictransit.divvy.DivvyStationEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PublicTransitController {
    private CtaPublicTransitService publicTransitService;
    private DivvyService divvyService;

    @Autowired
    public PublicTransitController(CtaPublicTransitService publicTransitService, DivvyService divvyService) {
        this.publicTransitService = publicTransitService;
        this.divvyService = divvyService;
    }

    @RequestMapping(value = "/api/public-transit/cta-timetable", produces = MediaType.APPLICATION_JSON_VALUE)
    public CtaTimeTableEntity showCtaTimeTable() {
        return publicTransitService.getCtaTimeTable();
    }

    @RequestMapping(value = "/api/public-transit/divvy-stations", produces = MediaType.APPLICATION_JSON_VALUE)
    public DivvyStationEntityWrapper listDivvyStations() {
        return new DivvyStationEntityWrapper(divvyService.findAllStations());
    }

    public static class DivvyStationEntityWrapper {
        private List<DivvyStationEntity> divvyStations;

        public DivvyStationEntityWrapper(List<DivvyStationEntity> divvyStations) {
            this.divvyStations = divvyStations;
        }

        public List<DivvyStationEntity> getDivvyStations() {
            return divvyStations;
        }
    }
}
