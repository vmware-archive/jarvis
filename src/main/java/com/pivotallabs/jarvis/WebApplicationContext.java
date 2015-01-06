package com.pivotallabs.jarvis;

import com.twilio.sdk.TwilioRestClient;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class WebApplicationContext {
    @Bean
    public HttpClient getHttpClient() {
        return HttpClients.createDefault();
    }

    @Bean
    public TwilioRestClient getTwilioRestClient(@Value("${twilio.accountID}") String accountID, @Value("${twilio.authToken}") String authToken) {
        return new TwilioRestClient(accountID, authToken);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(new SimpleClientHttpRequestFactory());
    }

}
