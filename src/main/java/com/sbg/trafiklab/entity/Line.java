package com.sbg.trafiklab.entity;

import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;


@Table("line")
public class Line {

    @Id
    private Long id;

    @Column("line_number")
    private String lineNumber;

    @Column("exists_from_date")
    private LocalDate existsFromDate;

    public String getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(String lineNumber) {
        this.lineNumber = lineNumber;
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
