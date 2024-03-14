package com.sbg.trafiklab.repository;

import com.sbg.trafiklab.entity.JourneyPattern;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface JourneyPatternRepository extends ReactiveCrudRepository<JourneyPattern, Long> {

    @Query("SELECT * FROM journey_pattern WHERE line_number = :lineNumber AND stop_point_number = :stopPointNumber AND direction = :direction ORDER BY exists_from_date DESC LIMIT 1")
    Mono<JourneyPattern> findByLineNumberAndStopPointNumberAndDirection(String lineNumber, String stopPointNumber,
            String direction);
}
