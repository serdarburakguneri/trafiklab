package com.sbg.trafiklab.mapper;


import com.sbg.trafiklab.integration.dto.SLJourneyPattern;
import com.sbg.trafiklab.entity.JourneyPattern;

public class JourneyPatternMapper {

    public static JourneyPattern fromSLJourneyPatter(SLJourneyPattern slJourneyPattern) {
        var journeyPattern = new JourneyPattern();
        journeyPattern.setLineNumber(Integer.toString(slJourneyPattern.lineNumber()));
        journeyPattern.setStopNumber(Integer.toString(slJourneyPattern.journeyPatternPointNumber()));
        journeyPattern.setDirection(Integer.toString(slJourneyPattern.directionCode()));
        journeyPattern.setExistsFromDate(slJourneyPattern.existsFromDate());
        return journeyPattern;
    }

}
