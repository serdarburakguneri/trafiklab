package com.sbg.trafiklab.mapper;

import com.sbg.trafiklab.dto.StopDTO;
import com.sbg.trafiklab.entity.Stop;

public class StopDTOMapper {

    public static StopDTO fromStop(Stop stop) {
        return new StopDTO(stop.getStopNumber(), stop.getStopName());
    }

}
