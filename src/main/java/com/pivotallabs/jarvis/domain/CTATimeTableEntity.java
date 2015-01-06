package com.pivotallabs.jarvis.domain;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "ctatt")
public class CTATimeTableEntity {

    public CTATimeTableEntity() {
    }

    public CTATimeTableEntity(List<CTAEtaEntity> predictions) {
        this.predictions = predictions;
    }

    @XmlElement(name = "eta")
    private List<CTAEtaEntity> predictions = new ArrayList<>();

    public List<CTAEtaEntity> getPredictions() {
        return predictions;
    }
}
