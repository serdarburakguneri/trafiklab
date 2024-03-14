package com.sbg.trafiklab.service;

import com.sbg.trafiklab.entity.JourneyPattern;
import com.sbg.trafiklab.repository.JourneyPatternRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class JourneyPatternService {

    private final JourneyPatternRepository journeyPatternRepository;
    private static final Logger logger = LoggerFactory.getLogger(JourneyPatternService.class);

    @Autowired
    public JourneyPatternService(JourneyPatternRepository journeyPatternRepository) {
        this.journeyPatternRepository = journeyPatternRepository;
    }

    public Mono<JourneyPattern> save(JourneyPattern journeyPattern) {
        return journeyPatternRepository.findByLineNumberAndStopPointNumberAndDirection(
                        journeyPattern.getLineNumber(),
                        journeyPattern.getStopPointNumber(),
                        journeyPattern.getDirection())
                .switchIfEmpty(
                        Mono.defer(() -> journeyPatternRepository.save(journeyPattern))
                                .doOnSuccess(savedJourneyPattern ->
                                        logger.info("Journey pattern saved: line : %s stop %s".formatted(
                                                savedJourneyPattern.getLineNumber(),
                                                savedJourneyPattern.getStopPointNumber())))
                                .onErrorResume(e -> {
                                    var message = "An error occurred while saving journey pattern.";
                                    logger.error(message, e);
                                    return Mono.error(new RuntimeException(message, e));
                                })
                );
    }

}
