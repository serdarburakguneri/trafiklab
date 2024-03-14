package com.sbg.trafiklab.entity;

import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("stop_point")
public class StopPoint {

    @Id
    private Long id;

    @Column("stop_point_number")
    private String stopPointNumber;

    @Column("stop_point_name")
    private String stopPointName;

    @Column("exists_from_date")
    private LocalDate existsFromDate;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getExistsFromDate() {
        return existsFromDate;
    }

    public void setExistsFromDate(LocalDate existsFromDate) {
        this.existsFromDate = existsFromDate;
    }
}
