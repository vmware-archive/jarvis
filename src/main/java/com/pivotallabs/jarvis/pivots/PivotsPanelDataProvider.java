package com.pivotallabs.jarvis.pivots;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Service
public class PivotsPanelDataProvider {
    private String pivotsFilename;

    @Autowired
    public PivotsPanelDataProvider(@Value("${pivots.filename}") String pivotsFilename) {
        this.pivotsFilename = pivotsFilename;
    }

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
