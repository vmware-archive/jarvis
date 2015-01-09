package com.pivotallabs.jarvis.projects.pivots;

import com.pivotallabs.jarvis.projects.domain.JarvisPivotEntity;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

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
        List<JarvisPivotEntity> actualPivots = pivotService.findAllPivots();

        assertThat(actualPivots, hasSize(2));

        JarvisPivotEntity pivot = actualPivots.get(0);
        assertThat(pivot.getName(), is("Gloria Coley"));
        assertThat(pivot.getEmail(), is("gcoley@example.com"));
        assertThat(pivot.getPhone(), is("+1123456789"));
    }
}