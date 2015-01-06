package com.pivotallabs.jarvis.server;

import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.resource.instance.Account;
import com.twilio.sdk.resource.instance.Message;
import com.twilio.sdk.resource.list.MessageList;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.BasicHttpEntity;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;

import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class PanelsControllerTests {
    private MockMvc mockMvc;

    @Mock
    private HttpClient mockHttpClient;

    @Mock
    private HttpUriRequest mockHttpGet;

    @Mock
    private HttpResponse mockHttpResponse;

    @Mock
    private TwilioRestClient mockTwilioRestClient;

    @Mock
    private Account mockTwilioAccount;

    @Mock
    private MessageList mockTwilioMessageList;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new PanelsController(
            mockHttpClient,
            mockTwilioRestClient,
            new RestTemplate(),
            "ctaApiKey",
            "allocationApiKey",
            0,
            "",
            "/pivots.json"
        )).build();
    }

    @Test
    public void testCTAPanelEndpointMapping() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/panels/cta"))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"));
    }

    @Test
    public void getsAllocationsData() throws Exception {
        InputStream allocationsInputStream = getClass().getResourceAsStream("/allocationsExample.json");

        BasicHttpEntity httpEntity = new BasicHttpEntity();
        httpEntity.setContent(allocationsInputStream);

        Mockito.when(mockHttpClient.execute(Mockito.any())).thenReturn(mockHttpResponse);
        Mockito.when(mockHttpResponse.getEntity()).thenReturn(httpEntity);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/panels/allocations"))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.projects", hasSize(2)))
            .andExpect(jsonPath("$.projects[0].name", is("Insurance Solutions App")))
            .andExpect(jsonPath("$.projects[0].pivotsByWeek", hasSize(1)))
            .andExpect(jsonPath("$.projects[0].pivotsByWeek[0]", hasSize(1)))
            .andExpect(jsonPath("$.projects[0].pivotsByWeek[0][0].name", is("Everett Nelson")))
            .andExpect(jsonPath("$.projects[1].name", is("Chicago Status Board")))
            .andExpect(jsonPath("$.projects[1].pivotsByWeek", hasSize(1)))
            .andExpect(jsonPath("$.projects[1].pivotsByWeek[0]", hasSize(2)))
            .andExpect(jsonPath("$.projects[1].pivotsByWeek[0][0].name", is("Mary Wilson")))
            .andExpect(jsonPath("$.projects[1].pivotsByWeek[0][1].name", is("Leona Farmer")));
    }

    @Test
    public void getsSMSData() throws Exception {
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

        mockMvc.perform(MockMvcRequestBuilders.get("/api/panels/sms-messages"))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.messages", hasSize(1)))
            .andExpect(jsonPath("$.messages[0].body", is("Test Message")))
            .andExpect(jsonPath("$.messages[0].from", is("+1123456789")))
            .andExpect(jsonPath("$.messages[0].dateSent", is(1413990633000L)))
            .andExpect(jsonPath("$.messages[0].direction", is("inbound")))
            .andExpect(jsonPath("$.messages[0].error", isEmptyOrNullString()));
    }

    @Test
    public void getsPivotsData() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/panels/pivots"))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.pivots", hasSize(1)))
            .andExpect(jsonPath("$.pivots[0].name", is("Gloria Coley")))
            .andExpect(jsonPath("$.pivots[0].email", is("gcoley@example.com")))
            .andExpect(jsonPath("$.pivots[0].phone", is("+1123456789")));
    }

    @Test
    public void respondsWith404ForUnknownPanel() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/panels/unknown-panel"))
            .andExpect(status().isNotFound());
    }
}