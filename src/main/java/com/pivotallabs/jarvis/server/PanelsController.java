package com.pivotallabs.jarvis.server;

import com.twilio.sdk.TwilioRestClient;
import org.apache.http.client.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
@EnableAutoConfiguration
@RequestMapping("/api/panels")
public class PanelsController {
    private Map<String, PanelDataProvider> panelDataProviders;

    @Autowired
    public PanelsController(
        HttpClient httpClient,
        TwilioRestClient twilioRestClient,
        RestTemplate restTemplate,
        @Value("${cta.apiKey}") String apiKey,
        @Value("${allocations.apiKey}") String allocationsApiKey,
        @Value("${allocations.accountID}") int allocationsAccountID,
        @Value("${allocations.emails}") String allocationsEmails,
        @Value("${pivots.filename}")String pivotsFilename) {
        this(new CTAPanelDataProvider(restTemplate, apiKey),
            new AllocationsPanelDataProvider(httpClient, allocationsApiKey, allocationsAccountID, allocationsEmails),
            new SMSMessagesPanelDataProvider(twilioRestClient),
            new PivotsPanelDataProvider(pivotsFilename));
    }

    public PanelsController(CTAPanelDataProvider ctaDataProvider,
                            AllocationsPanelDataProvider allocationsDataProvider,
                            SMSMessagesPanelDataProvider smsMessagesDataProvider,
                            PivotsPanelDataProvider pivotsDataProvider) {
        this.panelDataProviders = new HashMap<>();
        this.panelDataProviders.put("cta", ctaDataProvider);
        this.panelDataProviders.put("allocations", allocationsDataProvider);
        this.panelDataProviders.put("sms-messages", smsMessagesDataProvider);
        this.panelDataProviders.put("pivots", pivotsDataProvider);
    }

    @RequestMapping(value = "/{panelName}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Object panel(@PathVariable("panelName") String panelName, HttpServletResponse servletResponse) {
        servletResponse.addHeader("Access-Control-Allow-Origin", "*");
        PanelDataProvider dataProvider = this.panelDataProviders.get(panelName);
        if (dataProvider == null) {
            servletResponse.setStatus(HttpStatus.NOT_FOUND.value());
            return new HashMap<>();
        }
        return dataProvider.loadPanelData();
    }
}
