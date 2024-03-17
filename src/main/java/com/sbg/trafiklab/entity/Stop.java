package com.sbg.trafiklab.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Stop implements Serializable {

    private String stopNumber;

    private String stopName;

    private Date existsFromDate;

    public String getStopNumber() {
        return stopNumber;
    }

    public void setStopNumber(String stopNumber) {
        this.stopNumber = stopNumber;
    }

    public String getStopName() {
        return stopName;
    }

    public void setStopName(String stopName) {
        this.stopName = stopName;
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
        return Objects.equals(stopNumber, other.stopNumber) &&
                Objects.equals(stopName, other.stopName) &&
                Objects.equals(existsFromDate, other.existsFromDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stopNumber, stopName, existsFromDate);
    }
}
