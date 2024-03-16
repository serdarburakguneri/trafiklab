package com.sbg.trafiklab.entity;

import java.util.Date;
import java.util.Objects;

public class Stop {

    private String stopPointNumber;

    private String stopPointName;

    private Date existsFromDate;

    public String getStopPointNumber() {
        return stopPointNumber;
    }

    public void setStopPointNumber(String stopPointNumber) {
        this.stopPointNumber = stopPointNumber;
    }

    public String getStopPointName() {
        return stopPointName;
    }

    public void setStopPointName(String stopPointName) {
        this.stopPointName = stopPointName;
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
        Stop other = (Stop) obj;
        return Objects.equals(stopPointNumber, other.stopPointNumber) &&
                Objects.equals(stopPointName, other.stopPointName) &&
                Objects.equals(existsFromDate, other.existsFromDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stopPointNumber, stopPointName, existsFromDate);
    }
}
