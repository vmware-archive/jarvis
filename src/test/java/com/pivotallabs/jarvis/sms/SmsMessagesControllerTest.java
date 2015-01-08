package com.pivotallabs.jarvis.sms;

import com.pivotallabs.jarvis.sms.twilio.TwilioSmsMessageService;
import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.resource.instance.Account;
import com.twilio.sdk.resource.instance.Message;
import com.twilio.sdk.resource.list.MessageList;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class SmsMessagesControllerTest {
    private MockMvc mockMvc;

    @Mock
    private Account mockTwilioAccount;

    @Mock
    private MessageList mockTwilioMessageList;

    @Mock
    private TwilioRestClient mockTwilioRestClient;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        SmsMessageService provider = new TwilioSmsMessageService(mockTwilioRestClient);
        SmsMessagesController controller = new SmsMessagesController(provider);

        mockMvc = standaloneSetup(controller).build();
    }

    @Test
    public void listMessages_getsSmsData() throws Exception {
        List<Message> messages = new LinkedList<>();

        Map<String, Object> message1Properties = new HashMap<>();
        message1Properties.put("body", "Test Message");
        message1Properties.put("from", "+1123456789");
        message1Properties.put("date_sent", "Wed, 22 Oct 2014 10:10:33 CDT");
        message1Properties.put("direction", "inbound");
        Message exampleMessage1 = new Message(mockTwilioRestClient, message1Properties);
        messages.add(exampleMessage1);

        Map<String, Object> message2Properties = new HashMap<>();
        message2Properties.put("body", "Test Message 2");
        message2Properties.put("from", "+9876543210");
        message2Properties.put("date_sent", "Wed, 22 Oct 2014 10:10:33 CDT");
        message2Properties.put("direction", "outbound");
        Message exampleMessage2 = new Message(mockTwilioRestClient, message2Properties);
        messages.add(exampleMessage2);

        Mockito.when(mockTwilioRestClient.getAccount()).thenReturn(mockTwilioAccount);
        Mockito.when(mockTwilioAccount.getMessages(Mockito.any())).thenReturn(mockTwilioMessageList);
        Mockito.when(mockTwilioMessageList.iterator()).thenReturn(messages.iterator());

        mockMvc.perform(get("/api/sms-messages"))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.messages", hasSize(1)))
            .andExpect(jsonPath("$.messages[0].body", is("Test Message")))
            .andExpect(jsonPath("$.messages[0].from", is("+1123456789")))
            .andExpect(jsonPath("$.messages[0].dateSent", is(1413990633000L)))
            .andExpect(jsonPath("$.messages[0].direction", is("inbound")))
            .andExpect(jsonPath("$.messages[0].error", isEmptyOrNullString()));
    }
}