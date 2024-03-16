package com.sbg.trafiklab.mapper;

import com.sbg.trafiklab.dto.LineDTO;
import com.sbg.trafiklab.dto.StopDTO;
import com.sbg.trafiklab.entity.Line;
import java.util.stream.Collectors;

public class LineDTOMapper {

    public static LineDTO fromLine(Line line) {
        return new LineDTO(line.getLineNumber(),
                line.getStops().stream()
                        .map(stopPoint -> new StopDTO(stopPoint.getStopPointNumber(),
                                stopPoint.getStopPointName()))
                        .collect(Collectors.toList()));
    }

}
