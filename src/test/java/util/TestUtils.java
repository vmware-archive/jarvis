package util;

import com.pivotallabs.jarvis.WebApplicationContext;
import org.springframework.web.client.RestTemplate;

public class TestUtils {
    public static RestTemplate restTemplate() {
        WebApplicationContext webApplicationContext = new WebApplicationContext();

        return webApplicationContext.restTemplate(webApplicationContext.objectMapper());
    }
}
