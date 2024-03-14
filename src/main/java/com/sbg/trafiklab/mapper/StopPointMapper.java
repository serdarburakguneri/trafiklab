package com.sbg.trafiklab.mapper;

import com.sbg.trafiklab.integration.dto.SLStopPoint;
import com.sbg.trafiklab.entity.StopPoint;

public class StopPointMapper {

    public static StopPoint fromSLStopPoint(SLStopPoint slStopPoint) {
        var stopPoint = new StopPoint();
        stopPoint.setStopPointNumber(Integer.toString(slStopPoint.stopPointNumber()));
        stopPoint.setStopPointName(slStopPoint.stopPointName());
        stopPoint.setExistsFromDate(slStopPoint.existsFromDate());
        return stopPoint;
    }
}
