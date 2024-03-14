package com.sbg.trafiklab.integration.model;

public enum DefaultTransportModeCode {
    BUS("BUS"),
    METRO("METRO"),
    TRAIN("TRAIN"),
    TRAM("TRAM");

    private final String code;

    DefaultTransportModeCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
