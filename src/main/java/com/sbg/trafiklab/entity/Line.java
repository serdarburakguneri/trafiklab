package com.sbg.trafiklab.entity;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Line {

    private String lineNumber;

    private Date existsFromDate;

    private List<Stop> stops = List.of();

    public String getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(String lineNumber) {
        this.lineNumber = lineNumber;
    }

    public Date getExistsFromDate() {
        return existsFromDate;
    }

    public void setExistsFromDate(Date existsFromDate) {
        this.existsFromDate = existsFromDate;
    }

    public List<Stop> getStops() {
        return stops;
    }

    public void addStop(Stop stop) {
        stops.add(stop);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Line other = (Line) obj;
        return Objects.equals(lineNumber, other.lineNumber) &&
                Objects.equals(existsFromDate, other.existsFromDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lineNumber, existsFromDate);
    }
}
