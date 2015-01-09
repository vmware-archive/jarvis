package com.pivotallabs.jarvis.sms.twilio;

import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.resource.instance.Account;
import com.twilio.sdk.resource.instance.Message;
import com.twilio.sdk.resource.list.MessageList;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class TwilioSmsMessageServiceTest {
    @Mock
    private Account account;

    @Mock
    private MessageList messageList;

    @Mock
    private TwilioRestClient twilioRestClient;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void findAllSmsMessages_ReturnsSmsMessages() throws Exception {
        Map<String, Object> firstMessage = new HashMap<>();
        firstMessage.put("body", "Test Message");
        firstMessage.put("from", "+1123456789");
        firstMessage.put("date_sent", "Wed, 22 Oct 2014 10:10:33 CDT");
        firstMessage.put("direction", "inbound");

        Map<String, Object> secondMessage = new HashMap<>();
        secondMessage.put("body", "Test Message 2");
        secondMessage.put("from", "+9876543210");
        secondMessage.put("date_sent", "Wed, 22 Oct 2014 10:10:33 CDT");
        secondMessage.put("direction", "outbound");

        List<Message> expectedSmsMessages = new ArrayList<>();
        expectedSmsMessages.add(new Message(twilioRestClient, firstMessage));
        expectedSmsMessages.add(new Message(twilioRestClient, secondMessage));

        when(twilioRestClient.getAccount()).thenReturn(account);
        when(account.getMessages(any())).thenReturn(messageList);
        when(messageList.iterator()).thenReturn(expectedSmsMessages.iterator());

        TwilioSmsMessageService smsMessageService = new TwilioSmsMessageService(twilioRestClient);

        Map<String, Object> actualSmsMessages = smsMessageService.findAllSmsMessages();
        List<Map<String, Object>> messages = (List<Map<String, Object>>) actualSmsMessages.get("messages");
        assertThat(messages, hasSize(1));

        Map<String, Object> message = messages.get(0);
        assertThat(message.get("body"), is("Test Message"));
        assertThat(message.get("from"), is("+1123456789"));
        assertThat(message.get("dateSent"), is(new Date(1413990633000L)));
        assertThat(message.get("direction"), is("inbound"));
        assertThat(message.get("error"), is(nullValue()));
    }
}