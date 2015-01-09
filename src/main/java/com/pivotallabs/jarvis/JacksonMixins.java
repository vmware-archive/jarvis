package com.pivotallabs.jarvis;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JacksonMixins {
    public static class JarvisProjectMixin {
        @JsonProperty
        private int id;
        @JsonProperty
        private String name;
    }

    public static class JarvisAllocationMixin {
        @JsonProperty("person_id")
        private int personId;
        @JsonProperty("project_id")
        private int projectId;
        @JsonProperty("week_start")
        private String weekStart;
    }

    public static class JarvisEmployeeMixin {
        @JsonProperty
        private int id;
        @JsonProperty
        private String name;
    }

    public static class JarvisDivvyStationMixin {
        @JsonProperty
        private Integer id;
        @JsonProperty
        private String stationName;
        @JsonProperty
        private Integer availableBikes;
        @JsonProperty
        private Integer availableDocks;
    }
}
