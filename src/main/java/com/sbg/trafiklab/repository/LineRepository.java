package com.sbg.trafiklab.repository;

import com.sbg.trafiklab.entity.Line;
import com.sbg.trafiklab.entity.Stop;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LineRepository {

    Mono<Line> create(Line line);

    Mono<Line> addStopToLine(Line line, Stop stop);

    Mono<Line> findByLineNumber(String lineNumber);

    Flux<Line> findAll();

    Mono<Long> deleteAll();

    Mono<List<Stop>> fetchStopsOfLine(String lineNumber);

    Mono<Integer> fetchStopCountOfLine(String lineNumber);
}