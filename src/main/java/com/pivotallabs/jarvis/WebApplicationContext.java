package com.pivotallabs.jarvis;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pivotallabs.jarvis.projects.domain.JarvisAllocationEntity;
import com.pivotallabs.jarvis.projects.domain.JarvisPivotEntity;
import com.pivotallabs.jarvis.projects.domain.JarvisProjectEntity;
import com.pivotallabs.jarvis.publictransit.divvy.DivvyStationEntity;
import com.twilio.sdk.TwilioRestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
public class WebApplicationContext {
    @Bean
    public TwilioRestClient twilioRestClient(@Value("${twilio.accountId}") String accountId, @Value("${twilio.authToken}") String authToken) {
        return new TwilioRestClient(accountId, authToken);
    }

    @Bean
    public RestTemplate restTemplate(ObjectMapper objectMapper) {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper);

        RestTemplate restTemplate = new RestTemplate(new SimpleClientHttpRequestFactory());
        restTemplate.getMessageConverters().add(0, converter);

        return restTemplate;
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        objectMapper.addMixInAnnotations(JarvisProjectEntity.class, JacksonMixins.JarvisProjectMixin.class);
        objectMapper.addMixInAnnotations(JarvisAllocationEntity.class, JacksonMixins.JarvisAllocationMixin.class);
        objectMapper.addMixInAnnotations(JarvisPivotEntity.class, JacksonMixins.JarvisEmployeeMixin.class);
        objectMapper.addMixInAnnotations(DivvyStationEntity.class, JacksonMixins.JarvisDivvyStationMixin.class);

        return objectMapper;
    }
}
