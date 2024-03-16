package com.sbg.trafiklab.entity;

import java.util.Date;
import java.util.Objects;


public class JourneyPattern {

    private String lineNumber;

    private String stopPointNumber;

    private String direction;

    private Date existsFromDate;

    public String getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(String lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getStopPointNumber() {
        return stopPointNumber;
    }

    public void setStopPointNumber(String stopPointNumber) {
        this.stopPointNumber = stopPointNumber;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public Date getExistsFromDate() {
        return existsFromDate;
    }

    public void setExistsFromDate(Date existsFromDate) {
        this.existsFromDate = existsFromDate;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        JourneyPattern other = (JourneyPattern) obj;
        return Objects.equals(lineNumber, other.lineNumber) &&
                Objects.equals(stopPointNumber, other.stopPointNumber) &&
                Objects.equals(direction, other.direction) &&
                Objects.equals(existsFromDate, other.existsFromDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lineNumber, stopPointNumber, direction, existsFromDate);
    }
}
