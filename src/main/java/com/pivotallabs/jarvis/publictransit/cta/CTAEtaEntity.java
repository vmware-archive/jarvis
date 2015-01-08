package com.pivotallabs.jarvis.publictransit.cta;

import javax.xml.bind.annotation.XmlElement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CtaEtaEntity {
    private static final Map<String, String> lineIdToName;
    private static final SimpleDateFormat iso8601DateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    private static final SimpleDateFormat ctaDateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");

    static {
        lineIdToName = new HashMap<>();
        lineIdToName.put("P", "Purple");
        lineIdToName.put("Y", "Yellow");
        lineIdToName.put("G", "Green");
        lineIdToName.put("Brn", "Brown");
        lineIdToName.put("Org", "Orange");
    }

    @XmlElement(name = "rt")
    private String lineId;

    @XmlElement(name = "destNm")
    private String destination;

    @XmlElement(name = "staNm")
    private String station;

    @XmlElement(name = "arrT")
    private String upcomingTime;

    public String getDestination() {
        return destination;
    }

    public String getLine() {
        return lineIdToName.getOrDefault(lineId, lineId);
    }

    public String getLineId() {
        return lineId;
    }

    public String getStation() {
        return station;
    }

    public String getUpcomingTime() {
        Date upcomingDate;
        try {
            upcomingDate = ctaDateFormat.parse(upcomingTime);
        } catch (ParseException e) {
            return null;
        }

        return iso8601DateFormat.format(upcomingDate);
    }
}
