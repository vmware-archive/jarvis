package com.pivotallabs.jarvis.cta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CTAPanelController {
    private CTAPanelDataProvider ctaPanelDataProvider;

    @Autowired
    public CTAPanelController(CTAPanelDataProvider ctaPanelDataProvider) {
        this.ctaPanelDataProvider = ctaPanelDataProvider;
    }

    @RequestMapping(value = "/api/panels/cta", produces = MediaType.APPLICATION_JSON_VALUE)
    public CTATimeTableEntity show() {
        return ctaPanelDataProvider.loadPanelData();
    }
}
