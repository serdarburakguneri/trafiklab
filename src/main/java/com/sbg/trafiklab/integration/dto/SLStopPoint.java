package com.sbg.trafiklab.integration.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

public record SLStopPoint(
        @JsonProperty("StopPointNumber")
        int stopPointNumber,
        @JsonProperty("StopPointName")
        String stopPointName,
        @JsonProperty("StopAreaNumber")
        int stopAreaNumber,
        @JsonProperty("LocationNorthingCoordinate")
        double locationNorthingCoordinate,
        @JsonProperty("LocationEastingCoordinate")
        double locationEastingCoordinate,
        @JsonProperty("ZoneShortName")
        String zoneShortName,
        @JsonProperty("StopAreaTypeCode")
        String stopAreaTypeCode,
        @JsonProperty("LastModifiedUtcDateTime")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
        Date lastModifiedUtcDateTime,
        @JsonProperty("ExistsFromDate")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
        Date existsFromDate) {

}
