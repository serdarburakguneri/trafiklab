package com.sbg.trafiklab.mapper;

import com.sbg.trafiklab.integration.dto.SLStopPoint;
import com.sbg.trafiklab.entity.Stop;

public class StopMapper {

    public static Stop fromSLStopPoint(SLStopPoint slStopPoint) {
        var stopPoint = new Stop();
        stopPoint.setStopPointNumber(Integer.toString(slStopPoint.stopPointNumber()));
        stopPoint.setStopPointName(slStopPoint.stopPointName());
        stopPoint.setExistsFromDate(slStopPoint.existsFromDate());
        return stopPoint;
    }
}
