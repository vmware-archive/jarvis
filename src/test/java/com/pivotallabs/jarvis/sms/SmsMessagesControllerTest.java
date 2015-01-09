package com.pivotallabs.jarvis.sms;

import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.resource.instance.Account;
import com.twilio.sdk.resource.list.MessageList;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashMap;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class SmsMessagesControllerTest {
    private MockMvc mockMvc;

    @Mock
    private Account mockTwilioAccount;

    @Mock
    private MessageList mockTwilioMessageList;

    @Mock
    private TwilioRestClient mockTwilioRestClient;

    @Mock
    private SmsMessageService smsMessageService;

    private SmsMessagesController controller;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        controller = new SmsMessagesController(smsMessageService);

        mockMvc = standaloneSetup(controller).build();
    }

    @Test
    public void listMessages_EndpointMapping() throws Exception {
        when(smsMessageService.findAllSmsMessages()).thenReturn(new HashMap<>());

        mockMvc.perform(get("/api/sms-messages"))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json;charset=UTF-8"));
    }

    @Test
    public void listMessages_ReturnsSmsMessages() {
        HashMap<String, Object> expectedMessages = new HashMap<>();
        when(smsMessageService.findAllSmsMessages()).thenReturn(expectedMessages);

        Object actualMessages = controller.listMessages();

        assertThat(actualMessages, is(sameInstance(actualMessages)));
    }
}