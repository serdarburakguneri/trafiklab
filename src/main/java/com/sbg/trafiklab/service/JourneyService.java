package com.sbg.trafiklab.service;

import com.sbg.trafiklab.dto.JourneyDTO;
import com.sbg.trafiklab.dto.StopPointDTO;
import com.sbg.trafiklab.repository.LineRepository;
import com.sbg.trafiklab.repository.StopPointRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class JourneyService {

    private final LineRepository lineRepository;

    private final StopPointRepository stopPointRepository;

    @Autowired
    public JourneyService(LineRepository lineRepository, StopPointRepository stopPointRepository) {
        this.lineRepository = lineRepository;
        this.stopPointRepository = stopPointRepository;
    }

    public Flux<JourneyDTO> findAll(int limit) {

        return lineRepository.findAll()
                .map(line -> new JourneyDTO(line.getLineNumber(), List.of()))
                .flatMap(journey -> stopPointRepository.findByLineNumber(journey.lineNumber())
                        .map(stopPoint ->
                                new StopPointDTO(stopPoint.getStopPointNumber(), stopPoint.getStopPointName()))
                        .collectList()
                        .map(stopPoints -> new JourneyDTO(journey.lineNumber(), stopPoints)))
                .sort((journey1, journey2) -> Integer.compare(journey2.stops().size(),
                        journey1.stops().size()))
                .take(limit);
    }
}
