package com.pivotallabs.jarvis.publictransit.cta;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "ctatt")
public class CtaTimeTableEntity {

    public CtaTimeTableEntity() {
    }

    public CtaTimeTableEntity(List<CtaEtaEntity> predictions) {
        this.predictions = predictions;
    }

    @XmlElement(name = "eta")
    private List<CtaEtaEntity> predictions = new ArrayList<>();

    public List<CtaEtaEntity> getPredictions() {
        return predictions;
    }
}
