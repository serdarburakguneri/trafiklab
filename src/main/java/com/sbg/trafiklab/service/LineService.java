package com.sbg.trafiklab.service;

import com.sbg.trafiklab.entity.Line;
import com.sbg.trafiklab.repository.LineRepository;
import com.sbg.trafiklab.repository.StopRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class LineService {

    private final LineRepository lineRepository;
    private final StopRepository stopRepository;

    private static final Logger logger = LoggerFactory.getLogger(LineService.class);

    @Autowired
    public LineService(LineRepository lineRepository,
            StopRepository stopRepository) {
        this.lineRepository = lineRepository;
        this.stopRepository = stopRepository;
    }

    public Mono<Line> create(Line line) {
        return lineRepository.save(line);
    }

    public Mono<Line> save(Line line) {
        return lineRepository.save(line);
    }

    public Mono<Line> addStopToLine(String lineNumber, String stopNumber) {

        return lineRepository.findByLineNumber(lineNumber)
                .flatMap(line -> stopRepository.findByStopNumber(stopNumber)
                        .flatMap(stop -> lineRepository.addStopToLine(line, stop)));

    }

    public Mono<Long> getStopCountForLine(Line line) {
        return lineRepository.getStopCount(line);
    }


    public Mono<Line> findByLineNumber(String lineNumber) {
        return lineRepository.findByLineNumber(lineNumber);
    }

    public Flux<Line> findAll(int limit) {
        return lineRepository.findAll()
                .sort((line1, line2) ->
                        Long.compare(lineRepository.getStopCount(line2), lineRepository.getStopCount(line2)))
                .take(limit);
    }

}
