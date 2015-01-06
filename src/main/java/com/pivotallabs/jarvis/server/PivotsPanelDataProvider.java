package com.pivotallabs.jarvis.server;

import org.springframework.boot.json.JacksonJsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class PivotsPanelDataProvider implements PanelDataProvider {
    private String pivotsFilename;

    public PivotsPanelDataProvider(String pivotsFilename) {
        this.pivotsFilename = pivotsFilename;
    }

    @Override
    public Object loadPanelData() {
        try(InputStream pivotsInputStream = getClass().getResourceAsStream(pivotsFilename)) {
            BufferedReader bufferedContentReader = new BufferedReader(new InputStreamReader(pivotsInputStream));
            StringBuilder buffer = new StringBuilder();
            String jsonLine;
            while ((jsonLine = bufferedContentReader.readLine()) != null) {
                buffer.append(jsonLine);
            }
            JacksonJsonParser jsonParser = new JacksonJsonParser();
            return jsonParser.parseMap(buffer.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
