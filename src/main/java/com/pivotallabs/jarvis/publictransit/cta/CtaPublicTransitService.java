package com.pivotallabs.jarvis.publictransit.cta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.*;

@Service
public class CtaPublicTransitService {

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
    public CtaPublicTransitService(RestTemplate restTemplate, @Value("${cta.apiKey}") String apiKey) {
        this.restTemplate = restTemplate;
        this.apiKey = apiKey;
    }

    public CtaTimeTableApiResponse getCtaTimeTable() {
        String stopIdUrl = MessageFormat.format("{0}?key={1}&stpid={2}&max=100", CTA_API_URL, apiKey, CLARK_AND_LAKE_BLUE_LINE);
        ResponseEntity<CtaTimeTableApiResponse> entity1 = restTemplate.getForEntity(stopIdUrl, CtaTimeTableApiResponse.class);

        String mapIdUrl = MessageFormat.format("{0}?key={1}&mapid={2}&max=100", CTA_API_URL, apiKey, MERCHANDISE_MART_GRAND_AND_STATE);
        ResponseEntity<CtaTimeTableApiResponse> entity2 = restTemplate.getForEntity(mapIdUrl, CtaTimeTableApiResponse.class);

        List<JarvisEtaEntity> filteredTimeTableEtas = new ArrayList<>();
        filterTimeTableEtas(entity1.getBody(), filteredTimeTableEtas);
        filterTimeTableEtas(entity2.getBody(), filteredTimeTableEtas);

        return new CtaTimeTableApiResponse(filteredTimeTableEtas);
    }

    private void filterTimeTableEtas(CtaTimeTableApiResponse ctaTimeTableApiResponse, List<JarvisEtaEntity> filteredEtas) {
        for (JarvisEtaEntity ctaEtaEntity : ctaTimeTableApiResponse.getPredictions()) {
            String lineName = ctaEtaEntity.getLine();
            String destination = ctaEtaEntity.getDestination();

            if (lineNameToDesiredDestinations.get(lineName).contains(destination)) {
                filteredEtas.add(ctaEtaEntity);
            }
        }
    }
}
