package com.pivotallabs.jarvis.sms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SmsMessagesController {
    private SmsMessageService smsMessageService;

    @Autowired
    public SmsMessagesController(SmsMessageService smsMessageService) {
        this.smsMessageService = smsMessageService;
    }

    @RequestMapping("/api/sms-messages")
    public Object listMessages() {
        return smsMessageService.findAllSmsMessages();
    }

}
