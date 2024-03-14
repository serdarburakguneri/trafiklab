package com.sbg.trafiklab.dto;

import java.util.List;

public record JourneyDTO(String lineNumber, List<StopPointDTO> stops) {

}
