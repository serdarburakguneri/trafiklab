package com.sbg.trafiklab.integration.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

public record SLApiResponse<T>(
        @JsonProperty("StatusCode")
        int statusCode,
        @JsonProperty("Message")
        String Message,
        @JsonProperty("ExecutionTime")
        int executionTime,
        @JsonProperty("ResponseData")
        SLResponseData<T> responseData) {

}

