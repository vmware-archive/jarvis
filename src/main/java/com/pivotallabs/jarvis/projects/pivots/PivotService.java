package com.pivotallabs.jarvis.projects.pivots;

import com.pivotallabs.jarvis.projects.domain.JarvisPivotEntity;
import com.pivotallabs.jarvis.util.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Service
public class PivotService {
    private String pivotsFilename;

    @Autowired
    public PivotService(@Value("${pivots.filename}") String pivotsFilename) {
        this.pivotsFilename = pivotsFilename;
    }

    public List<JarvisPivotEntity> findAllPivots() {
        JacksonJsonParser jsonParser = new JacksonJsonParser();
        try {
            Map<String, Object> map = jsonParser.parseMap(FileUtils.readFile(pivotsFilename));
            List<Map<String, Object>> pivots = (List<Map<String, Object>>) map.get("pivots");

            return pivots.stream()
                .map(json -> {
                    JarvisPivotEntity pivot = new JarvisPivotEntity();
                    pivot.setName((String) json.get("name"));
                    pivot.setEmail((String) json.get("email"));
                    pivot.setPhone((String) json.get("phone"));
                    return pivot;
                })
                .collect(toList());
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }
}
