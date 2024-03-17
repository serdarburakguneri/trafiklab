package com.sbg.trafiklab.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;


public class JourneyPattern implements Serializable {

    private String lineNumber;

    private String stopNumber;

    private String direction;

    private Date existsFromDate;

    public String getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(String lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getStopNumber() {
        return stopNumber;
    }

    public void setStopNumber(String stopNumber) {
        this.stopNumber = stopNumber;
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
                Objects.equals(stopNumber, other.stopNumber) &&
                Objects.equals(direction, other.direction) &&
                Objects.equals(existsFromDate, other.existsFromDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lineNumber, stopNumber, direction, existsFromDate);
    }
}
