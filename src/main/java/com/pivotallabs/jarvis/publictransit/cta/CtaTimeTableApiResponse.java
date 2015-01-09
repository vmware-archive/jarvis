package com.pivotallabs.jarvis.publictransit.cta;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "ctatt")
public class CtaTimeTableApiResponse {
    @XmlElement(name = "eta")
    private List<JarvisEtaEntity> predictions = new ArrayList<>();

    public CtaTimeTableApiResponse() {
    }

    public CtaTimeTableApiResponse(List<JarvisEtaEntity> predictions) {
        this.predictions = predictions;
    }

    public List<JarvisEtaEntity> getPredictions() {
        return predictions;
    }
}
