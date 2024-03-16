package com.sbg.trafiklab.dto;

import java.util.List;

public record LineDTO(String lineNumber, List<StopDTO> stops) {

}
