package com.pivotallabs.jarvis.publictransit.divvy;

public class DivvyStationEntity {
    private Integer id;
    private String stationName;
    private Integer availableBikes;
    private Integer availableDocks;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public Integer getAvailableBikes() {
        return availableBikes;
    }

    public void setAvailableBikes(Integer availableBikes) {
        this.availableBikes = availableBikes;
    }

    public Integer getAvailableDocks() {
        return availableDocks;
    }

    public void setAvailableDocks(Integer availableDocks) {
        this.availableDocks = availableDocks;
    }
}
