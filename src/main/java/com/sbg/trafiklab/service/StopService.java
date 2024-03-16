package com.sbg.trafiklab.service;

import com.sbg.trafiklab.entity.Stop;
import com.sbg.trafiklab.repository.StopRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class StopService {

    private final StopRepository stopRepository;

    private static final Logger logger = LoggerFactory.getLogger(StopService.class);

    @Autowired
    public StopService(StopRepository stopRepository) {
        this.stopRepository = stopRepository;
    }

    public Mono<Stop> create(Stop stop) {
        return stopRepository.save(stop);
    }

    public Mono<Stop> findByStopPointNumber(String stopPointNumber) {
        return stopRepository.findByStopNumber(stopPointNumber);
    }

    public Flux<Stop> findAll(int limit) {
        return stopRepository.findAll()
                .take(limit);
    }

}
