package com.sbg.trafiklab.repository;

import com.sbg.trafiklab.entity.StopPoint;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface StopPointRepository extends ReactiveCrudRepository<StopPoint, Long> {

    @Query("SELECT * FROM stop_point WHERE stop_point_number = :stopPointNumber ORDER BY exists_from_date DESC LIMIT 1")
    Mono<StopPoint> findByStopPointNumber(String stopPointNumber);

    @Query("SELECT sp.* FROM stop_point sp " +
            "INNER JOIN journey_pattern jp ON sp.stop_point_number = jp.stop_point_number " +
            "WHERE jp.line_number = :lineNumber")
    Flux<StopPoint> findByLineNumber(String lineNumber);

}
