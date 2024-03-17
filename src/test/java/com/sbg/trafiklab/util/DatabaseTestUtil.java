package com.sbg.trafiklab.util;

import com.sbg.trafiklab.dto.LineDTO;
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

    public void clearDatabase() {
        lineService.deleteAll().block();
        stopService.deleteAll().block();
    }

    public void createDataSet(List<LineDTO> lines) {
        lines.forEach(lineDTO -> {
            var modifyDate = new Date();
            var line = new Line();
            line.setLineNumber(lineDTO.lineNumber());
            line.setExistsFromDate(modifyDate);
            lineService.create(line).block();

            lineDTO.stops().forEach(stopDTO -> {
                var stop = new Stop();
                stop.setStopNumber(stopDTO.stopPointNumber());
                stop.setStopName(stopDTO.stopPointName());
                stop.setExistsFromDate(modifyDate);
                stopService.create(stop).block();

                lineService.addStopToLine(line.getLineNumber(), stop.getStopNumber()).block();
            });
        });
    }
}
