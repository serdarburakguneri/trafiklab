package com.sbg.trafiklab.repository;

import com.sbg.trafiklab.entity.Line;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface LineRepository extends ReactiveCrudRepository<Line, Long> {

    @Query("SELECT * FROM line WHERE line_number = :lineNumber ORDER BY exists_from_date DESC LIMIT 1")
    Mono<Line> findByLineNumber(String lineNumber);

}
