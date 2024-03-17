package com.sbg.trafiklab.repository;

import com.sbg.trafiklab.entity.Stop;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface StopRepository {

    Mono<Stop> create(Stop stop);

    Mono<Stop> findByStopNumber(String stopNumber);

    Flux<Stop> findAll();

    Mono<Long> deleteAll();

}