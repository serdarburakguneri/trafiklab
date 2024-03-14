package com.sbg.trafiklab.integration.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record SLResponseData<T>(
        @JsonProperty("Version")
        String Version,
        @JsonProperty("Type")
        String modelType,
        @JsonProperty("Result")
        List<T> result) {

}
