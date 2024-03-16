package com.sbg.trafiklab.integration.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.util.Date;


public record SLLineData(
        @JsonProperty("LineNumber")
        int LineNumber,
        @JsonProperty("LineDesignation")
        String LineDesignation,
        @JsonProperty("DefaultTransportMode")
        String DefaultTransportMode,
        @JsonProperty("DefaultTransportModeCode")
        String DefaultTransportModeCode,
        @JsonProperty("LastModifiedUtcDateTime")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
        Date LastModifiedUtcDateTime,
        @JsonProperty("ExistsFromDate")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
        Date ExistsFromDate) {

}
