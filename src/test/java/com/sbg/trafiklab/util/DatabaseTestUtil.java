package com.sbg.trafiklab.util;

import com.sbg.trafiklab.dto.LineDTO;
import com.sbg.trafiklab.entity.JourneyPattern;
import com.sbg.trafiklab.entity.Line;
import com.sbg.trafiklab.entity.Stop;
import com.sbg.trafiklab.service.LineService;
import com.sbg.trafiklab.service.StopService;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DatabaseTestUtil {

    private final LineService lineService;

    private final StopService stopService;


    @Autowired
    public DatabaseTestUtil(LineService lineService, StopService stopService) {
        this.lineService = lineService;
        this.stopService = stopService;
    }


    public void createDataSet(List<LineDTO> journeys) {
        journeys.forEach(journey -> {
            var modifyDate = new Date();
            var line = new Line();
            line.setLineNumber(journey.lineNumber());
            line.setExistsFromDate(modifyDate);
            lineService.create(line).block();

            journey.stops().forEach(stop -> {
                var stopPoint = new Stop();
                stopPoint.setStopPointNumber(stop.stopPointNumber());
                stopPoint.setStopPointName(stop.stopPointName());
                stopPoint.setExistsFromDate(modifyDate);
                stopService.create(stopPoint).block();

                var journeyPattern = new JourneyPattern();
                journeyPattern.setLineNumber(journey.lineNumber());
                journeyPattern.setStopPointNumber(stop.stopPointNumber());
                journeyPattern.setDirection("1");
                journeyPattern.setExistsFromDate(modifyDate);
            });
        });
    }
}
