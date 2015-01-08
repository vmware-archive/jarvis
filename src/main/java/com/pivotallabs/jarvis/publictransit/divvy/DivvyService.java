package com.pivotallabs.jarvis.publictransit.divvy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class DivvyService {
    private static final String DIVVY_API_URL = "http://www.divvybikes.com/stations/json";
    private static final List<Integer> NEARBY_STATIONS = new ArrayList<>();

    private RestTemplate restTemplate;

    @Autowired
    public DivvyService(RestTemplate restTemplate, @Value("${divvy.stationIds}") String nearbyDivvyStationIds) {
        this.restTemplate = restTemplate;

        if (nearbyDivvyStationIds == null) {
            throw new IllegalArgumentException("divvy.stationIds can't be null");
        }

        NEARBY_STATIONS.addAll(
            Arrays.stream(nearbyDivvyStationIds.split(",\\s*"))
                .map(Integer::parseInt)
                .collect(toList())
        );
    }

    public List<DivvyStationEntity> findAllStations() {
        DivvyApiResponse apiResponse = restTemplate.getForObject(DIVVY_API_URL, DivvyApiResponse.class);

        return apiResponse.getStations().stream()
            .filter(station -> NEARBY_STATIONS.contains(station.getId()))
            .collect(toList());
    }
}
