package com.sbg.trafiklab.integration.model;

public enum DataModelType {
    STOP_POINT("stop"),
    JOURNEY_PATTERN("JourneyPattern"),
    LINE("Line");

    private final String type;

    DataModelType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
