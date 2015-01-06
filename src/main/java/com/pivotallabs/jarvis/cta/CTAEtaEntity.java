package com.pivotallabs.jarvis.cta;

import javax.xml.bind.annotation.XmlElement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CTAEtaEntity {
    private static final Map<String, String> lineIDToName;
    private static final SimpleDateFormat iso8601DateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    private static final SimpleDateFormat ctaDateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");

    static {
        lineIDToName = new HashMap<>();
        lineIDToName.put("P", "Purple");
        lineIDToName.put("Y", "Yellow");
        lineIDToName.put("G", "Green");
        lineIDToName.put("Brn", "Brown");
        lineIDToName.put("Org", "Orange");
    }

    @XmlElement(name = "rt")
    private String lineID;

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
        return lineIDToName.getOrDefault(lineID, lineID);
    }

    public String getLineID() {
        return lineID;
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
