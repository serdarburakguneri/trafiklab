package com.sbg.trafiklab.service;

import com.sbg.trafiklab.entity.StopPoint;
import com.sbg.trafiklab.repository.StopPointRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class StopPointService {

    private final StopPointRepository stopPointRepository;

    private static final Logger logger = LoggerFactory.getLogger(StopPointService.class);

    @Autowired
    public StopPointService(StopPointRepository stopRepository) {
        this.stopPointRepository = stopRepository;
    }

    public Mono<StopPoint> save(StopPoint stopPoint) {
        return stopPointRepository.findByStopPointNumber(stopPoint.getStopPointNumber())
                .switchIfEmpty(
                        Mono.defer(() -> stopPointRepository.save(stopPoint))
                                .doOnSuccess(savedStopPoint -> logger.info(
                                        "Stop point saved. Stop point number %s.".formatted(
                                                savedStopPoint.getStopPointNumber())))
                                .onErrorResume(e -> {
                                    var message = "An error occurred while saving stop point.";
                                    logger.error(message, e);
                                    return Mono.error(new RuntimeException(message, e));
                                })
                );
    }

}
