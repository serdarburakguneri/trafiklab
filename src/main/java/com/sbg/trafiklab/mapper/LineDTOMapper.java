package com.sbg.trafiklab.mapper;

import com.sbg.trafiklab.dto.LineDTO;
import com.sbg.trafiklab.entity.Line;
import java.util.stream.Collectors;

public class LineDTOMapper {

    public static LineDTO fromLine(Line line) {

        return new LineDTO(line.getLineNumber(),
                line.getStopCount(),
                line.getStops()
                        .stream()
                        .map(StopDTOMapper::fromStop)
                        .collect(Collectors.toList()));
    }

}
