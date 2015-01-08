package com.pivotallabs.jarvis.publictransit.divvy;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class DivvyApiResponse {
    @JsonProperty("stationBeanList")
    private List<DivvyStationEntity> stations;

    public List<DivvyStationEntity> getStations() {
        return stations;
    }

    public void setStations(List<DivvyStationEntity> stations) {
        this.stations = stations;
    }
}
