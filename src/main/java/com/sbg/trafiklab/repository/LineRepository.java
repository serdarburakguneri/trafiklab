package com.sbg.trafiklab.repository;

import com.sbg.trafiklab.entity.Line;
import com.sbg.trafiklab.entity.Stop;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LineRepository {

    Mono<Line> save(Line line);

    Mono<Line> addStopToLine(Line line, Stop stop);

    Mono<Line> findByLineNumber(String id);

    Flux<Line> findAll();

    Mono<Long> deleteAll();
    Mono<Long> getStopCount(Line line);
}