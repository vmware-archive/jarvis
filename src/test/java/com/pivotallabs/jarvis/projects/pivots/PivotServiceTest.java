package com.pivotallabs.jarvis.projects.pivots;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class PivotServiceTest {
    private PivotService pivotService;

    @Before
    public void setUp() throws Exception {
        pivotService = new PivotService("/pivots.json");
    }

    @Test
    public void findAllPivots_ReturnsPivots() {
        Map<String, Object> actualPivots = pivotService.findAllPivots();

        List<Map<String, Object>> pivots = (List<Map<String, Object>>) actualPivots.get("pivots");
        assertThat(pivots, hasSize(1));

        Map<String, Object> pivot = pivots.get(0);
        assertThat(pivot.get("name"), is("Gloria Coley"));
        assertThat(pivot.get("email"), is("gcoley@example.com"));
        assertThat(pivot.get("phone"), is("+1123456789"));
    }
}