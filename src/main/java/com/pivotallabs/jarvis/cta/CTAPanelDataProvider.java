package com.pivotallabs.jarvis.cta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.*;

@Service
public class CTAPanelDataProvider {

    private static final String CTA_API_URL = "http://lapi.transitchicago.com/api/1.0/ttarrivals.aspx";
    private static final String CLARK_AND_LAKE_BLUE_LINE = "30374,30375"; // Clark/Lake Blue
    private static final String MERCHANDISE_MART_GRAND_AND_STATE = "40330,40460"; // Merchandise Mart, Grand-Red

    private static final Map<String, List<String>> lineNameToDesiredDestinations;

    private RestTemplate restTemplate;
    private String apiKey;

    static {
        lineNameToDesiredDestinations = new HashMap<>();
        lineNameToDesiredDestinations.put("Blue", Collections.singletonList("O'Hare"));
        lineNameToDesiredDestinations.put("Red", Collections.singletonList("Howard"));
        lineNameToDesiredDestinations.put("Brown", Collections.singletonList("Kimball"));
        lineNameToDesiredDestinations.put("Purple", Collections.singletonList("Linden"));
    }

    @Autowired
    public CTAPanelDataProvider(RestTemplate restTemplate, @Value("${cta.apiKey}") String apiKey) {
        this.restTemplate = restTemplate;
        this.apiKey = apiKey;
    }

    public CTATimeTableEntity loadPanelData() {
        String stopIDUrl = MessageFormat.format("{0}?key={1}&stpid={2}&max=100", CTA_API_URL, apiKey, CLARK_AND_LAKE_BLUE_LINE);
        ResponseEntity<CTATimeTableEntity> entity1 = restTemplate.getForEntity(stopIDUrl, CTATimeTableEntity.class);

        String mapIDUrl = MessageFormat.format("{0}?key={1}&mapid={2}&max=100", CTA_API_URL, apiKey, MERCHANDISE_MART_GRAND_AND_STATE);
        ResponseEntity<CTATimeTableEntity> entity2 = restTemplate.getForEntity(mapIDUrl, CTATimeTableEntity.class);

        List<CTAEtaEntity> filteredTimeTableEtas = new ArrayList<>();
        filterTimeTableEtas(entity1.getBody(), filteredTimeTableEtas);
        filterTimeTableEtas(entity2.getBody(), filteredTimeTableEtas);

        return new CTATimeTableEntity(filteredTimeTableEtas);
    }

    private void filterTimeTableEtas(CTATimeTableEntity ctaTimeTableEntity, List<CTAEtaEntity> filteredEtas) {
        for (CTAEtaEntity ctaEtaEntity : ctaTimeTableEntity.getPredictions()) {
            String lineName = ctaEtaEntity.getLine();
            String destination = ctaEtaEntity.getDestination();

            if (lineNameToDesiredDestinations.get(lineName).contains(destination)) {
                filteredEtas.add(ctaEtaEntity);
            }
        }
    }
}
