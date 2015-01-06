package com.pivotallabs.jarvis.sms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SMSPanelController {
    private SMSMessagesPanelDataProvider smsMessagesPanelDataProvider;

    @Autowired
    public SMSPanelController(SMSMessagesPanelDataProvider smsMessagesPanelDataProvider) {
        this.smsMessagesPanelDataProvider = smsMessagesPanelDataProvider;
    }

    @RequestMapping("/api/panels/sms-messages")
    public Object show() {
        return smsMessagesPanelDataProvider.loadPanelData();
    }

}
