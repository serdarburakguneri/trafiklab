package com.sbg.trafiklab.mapper;

import com.sbg.trafiklab.integration.dto.SLLineData;
import com.sbg.trafiklab.entity.Line;

public class LineMapper {

    public static Line fromSLLineData(SLLineData slLineData) {
        var line = new Line();
        line.setLineNumber(Integer.toString(slLineData.LineNumber()));
        line.setExistsFromDate(slLineData.ExistsFromDate());
        return line;
    }
}
