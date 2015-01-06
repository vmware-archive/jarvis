package com.pivotallabs.jarvis.server;

import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.resource.instance.Message;
import com.twilio.sdk.resource.list.MessageList;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class SMSMessagesPanelDataProvider implements PanelDataProvider {

    private TwilioRestClient twilioRestClient;
    private DateFormat todayFormat;

    public SMSMessagesPanelDataProvider(TwilioRestClient twilioRestClient) {
        this.twilioRestClient = twilioRestClient;
        this.todayFormat = new SimpleDateFormat("YYYY-MM-dd");
        this.todayFormat.setTimeZone(TimeZone.getTimeZone("CDT"));
    }

    @Override
    public Object loadPanelData() {
        Map<String, Object> jsonRootObject = new HashMap<>();
        List<Map<String, Object>> jsonMessages = new LinkedList<>();
        jsonRootObject.put("messages", jsonMessages);

        Map<String, String> twilioRequestFilters = new HashMap<>();
        twilioRequestFilters.put("DateSent", todayFormat.format(new Date()));

        MessageList messages = twilioRestClient.getAccount().getMessages(twilioRequestFilters);
        for (Message message : messages) {
            if ("inbound".equals(message.getDirection())) {
                Map<String, Object> jsonMessage = new HashMap<>();
                jsonMessage.put("body", message.getBody());
                jsonMessage.put("dateSent", message.getDateSent());
                jsonMessage.put("error", message.getErrorMessage());
                jsonMessage.put("from", message.getFrom());
                jsonMessage.put("direction", message.getDirection());
                jsonMessages.add(jsonMessage);
            }
        }

        return jsonRootObject;
    }
}
