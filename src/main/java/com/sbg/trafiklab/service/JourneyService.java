package com.sbg.trafiklab.service;

import com.sbg.trafiklab.dto.JourneyDTO;
import com.sbg.trafiklab.dto.StopPointDTO;
import com.sbg.trafiklab.repository.LineRepository;
import com.sbg.trafiklab.repository.StopPointRepository;
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

        // Seeing we can't have many-to-many relationships in R2DBC,
        // we have to fetch all lines and then fetch all stop points to be able to aggregate them into a JourneyDTO

        return lineRepository.findAll()
                .flatMap(line -> stopPointRepository.findByLineNumber(line.getLineNumber())
                        .map(stopPoint ->
                                new StopPointDTO(stopPoint.getStopPointNumber(), stopPoint.getStopPointName()))
                        .collectList()
                        .map(stopPoints -> new JourneyDTO(line.getLineNumber(), stopPoints)))
                .sort((journey1, journey2) -> Integer.compare(journey2.stops().size(),
                        journey1.stops().size()))
                .take(limit);
    }
}
