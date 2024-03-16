package com.sbg.trafiklab.repository;

import com.sbg.trafiklab.entity.Line;
import com.sbg.trafiklab.entity.Stop;
import com.sbg.trafiklab.exception.EntityNotFoundException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class LineRepositoryRedisImpl implements LineRepository {

    private static final String LINE_ENTITY_KEY_PREFIX = "line:";
    private final ReactiveRedisOperations<String, Line> lineRedisOps;
    private final ReactiveRedisOperations<String, Stop> stopRedisOps;

    private static final Logger logger = LoggerFactory.getLogger(LineRepositoryRedisImpl.class);

    @Autowired
    public LineRepositoryRedisImpl(ReactiveRedisOperations<String, Line> lineRedisOps,
            ReactiveRedisOperations<String, Stop> stopRedisOps) {
        this.lineRedisOps = lineRedisOps;
        this.stopRedisOps = stopRedisOps;
    }

    @Override
    public Mono<Line> save(Line line) {
        var key = LINE_ENTITY_KEY_PREFIX + line.getLineNumber();

        return lineRedisOps.opsForValue()
                .set(key, line)
                .flatMap(success -> {
                    if (success) {
                        return Mono.just(line);
                    } else {
                        return Mono.error(new RuntimeException("Failed to save line to Redis"));
                    }
                })
                .doOnSuccess(savedLine -> logger.info("Saved line with number: {}", line.getLineNumber()))
                .doOnError(e -> logger.error("Error saving line with number: {}", line.getLineNumber(), e));
    }

    @Override
    public Mono<Line> addStopToLine(Line line, Stop stop) {
        String key = "line_stops:" + line.getLineNumber();
        return stopRedisOps.opsForList().rightPush(key, stop).then(Mono.just(line));
    }

    @Override
    public Mono<Line> findByLineNumber(String lineNumber) {
        return lineRedisOps.opsForValue().get(LINE_ENTITY_KEY_PREFIX + lineNumber)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Line not found for number: " + lineNumber)));
    }

    public Mono<Line> fetchLineWithStops(String lineNumber) {
        return lineRedisOps.opsForValue().get(LINE_ENTITY_KEY_PREFIX + lineNumber)
                .flatMap(line ->
                        stopRedisOps.opsForList().range("line_stops:" + lineNumber, 0, -1)
                                .collectList()
                                .map(stops -> {
                                    line.getStops().addAll(stops);
                                    return line;
                                }));

    }


    @Override
    public Flux<Line> findAll() {
        return lineRedisOps.keys(LINE_ENTITY_KEY_PREFIX + "*")
                .flatMap(key -> fetchLineWithStops(key.substring(LINE_ENTITY_KEY_PREFIX.length())));
    }

    @Override
    public Mono<Long> deleteAll() {
        return lineRedisOps.delete(lineRedisOps.keys(LINE_ENTITY_KEY_PREFIX + "*"));
    }

    @Override
    public Long getStopCount(Line line) {
        return lineRedisOps.keys("line_stops:*").count().block();
    }
}