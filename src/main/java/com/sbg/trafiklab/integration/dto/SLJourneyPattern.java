package com.sbg.trafiklab.integration.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;

public record SLJourneyPattern(
        @JsonProperty("LineNumber")
        int lineNumber,
        @JsonProperty("DirectionCode")
        int directionCode,
        @JsonProperty("JourneyPatternPointNumber")
        int journeyPatternPointNumber,
        @JsonProperty("LastModifiedUtcDateTime")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
        LocalDate lastModifiedUtcDateTime,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
        @JsonProperty("ExistsFromDate")
        LocalDate existsFromDate) {

}
