package com.sbg.trafiklab.service;

import com.sbg.trafiklab.entity.Line;
import com.sbg.trafiklab.exception.EntityCreationFailureException;
import com.sbg.trafiklab.exception.EntityNotFoundException;
import com.sbg.trafiklab.exception.EntityUpdateException;
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
        return lineRepository.create(line)
                .onErrorResume(e -> {
                    var message = "An error occurred while creating line.";
                    logger.error(message, e);
                    return Mono.error(new EntityCreationFailureException(message, e));
                });
    }

    public Mono<Line> addStopToLine(String lineNumber, String stopNumber) {
        return lineRepository.findByLineNumber(lineNumber)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Line not found with number: " + lineNumber)))
                .flatMap(line -> stopRepository.findByStopNumber(stopNumber)
                        .switchIfEmpty(
                                Mono.error(new EntityNotFoundException("Stop not found with number: " + stopNumber)))
                        .flatMap(stop -> lineRepository.addStopToLine(line, stop)))
                .onErrorResume(e -> {
                    var message = "An error occurred while adding stop to line.";
                    logger.error(message, e);
                    return Mono.error(new EntityUpdateException(message, e));
                });
    }


    public Mono<Line> findByLineNumber(String lineNumber) {
        return lineRepository.findByLineNumber(lineNumber)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Line not found with number: " + lineNumber)))
                .flatMap(line -> lineRepository.fetchStopsOfLine(line.getLineNumber())
                        .map(stops -> {
                            line.setStops(stops);
                            return line;
                        }))
                .flatMap(line -> lineRepository.fetchStopCountOfLine(line.getLineNumber())
                        .map(stopCount -> {
                            line.setStopCount(stopCount);
                            return line;
                        }))
                .onErrorResume(e -> {
                    var message = "An error occurred while finding line by number.";
                    logger.error(message, e);
                    return Mono.error(new EntityUpdateException(message, e));
                });
    }


    public Flux<Line> findAll(int limit) {
        return lineRepository.findAll()
                .flatMap(line -> lineRepository.fetchStopCountOfLine(line.getLineNumber())
                        .map(stopCount -> {
                            line.setStopCount(stopCount);
                            return line;
                        }))
                .sort((line1, line2) ->
                        Integer.compare(line2.getStopCount(), line1.getStopCount()))
                .take(limit)
                .flatMap(line -> lineRepository.fetchStopsOfLine(line.getLineNumber())
                        .map(stops -> {
                            line.getStops().addAll(stops);
                            return line;
                        }));
    }

    public Mono<Long> deleteAll() {
        return lineRepository.deleteAll();
    }

}
