package com.sbg.trafiklab.util;


import com.sbg.trafiklab.dto.JourneyDTO;
import com.sbg.trafiklab.entity.JourneyPattern;
import com.sbg.trafiklab.entity.Line;
import com.sbg.trafiklab.entity.StopPoint;
import com.sbg.trafiklab.service.JourneyPatternService;
import com.sbg.trafiklab.service.LineService;
import com.sbg.trafiklab.service.StopPointService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DatabaseTestUtil {

    private final LineService lineService;

    private final StopPointService stopPointService;

    private final JourneyPatternService journeyPatternService;

    @Autowired
    public DatabaseTestUtil(LineService lineService, StopPointService stopPointService,
            JourneyPatternService journeyPatternService) {
        this.lineService = lineService;
        this.stopPointService = stopPointService;
        this.journeyPatternService = journeyPatternService;
    }


    public void createDataSet(List<JourneyDTO> journeys) {
        journeys.forEach(journey -> {
            var modifyDate = LocalDate.now().minusDays(1);
            var line = new Line();
            line.setLineNumber(journey.lineNumber());
            line.setExistsFromDate(modifyDate);
            lineService.save(line).block();

            journey.stops().forEach(stop -> {
                var stopPoint = new StopPoint();
                stopPoint.setStopPointNumber(stop.stopPointNumber());
                stopPoint.setStopPointName(stop.stopPointName());
                stopPoint.setExistsFromDate(modifyDate);
                stopPointService.save(stopPoint).block();

                var journeyPattern = new JourneyPattern();
                journeyPattern.setLineNumber(journey.lineNumber());
                journeyPattern.setStopPointNumber(stop.stopPointNumber());
                journeyPattern.setDirection("1");
                journeyPattern.setExistsFromDate(modifyDate);
                journeyPatternService.save(journeyPattern).block();
            });
        });
    }
}
