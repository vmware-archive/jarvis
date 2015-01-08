package com.pivotallabs.jarvis.sms.twilio;

import com.pivotallabs.jarvis.sms.SmsMessageService;
import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.resource.instance.Message;
import com.twilio.sdk.resource.list.MessageList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class TwilioSmsMessageService implements SmsMessageService {

    private TwilioRestClient twilioRestClient;
    private DateFormat todayFormat;

    @Autowired
    public TwilioSmsMessageService(TwilioRestClient twilioRestClient) {
        this.twilioRestClient = twilioRestClient;
        this.todayFormat = new SimpleDateFormat("YYYY-MM-dd");
        this.todayFormat.setTimeZone(TimeZone.getTimeZone("CDT"));
    }

    public Object findAllSmsMessages() {
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
