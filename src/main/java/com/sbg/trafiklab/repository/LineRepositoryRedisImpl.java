package com.sbg.trafiklab.repository;

import com.sbg.trafiklab.entity.Line;
import com.sbg.trafiklab.entity.Stop;
import com.sbg.trafiklab.exception.EntityCreationFailureException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class LineRepositoryRedisImpl implements LineRepository {

    private static final String LINE_ENTITY_KEY_PREFIX = "line:%s";
    private static final String LINE_STOP_COUNT_KEY = "line_stop_count:%s";
    private static final String LINE_STOPS_KEY = "line_stops:%s";
    private final ReactiveRedisOperations<String, Line> lineRedisOps;
    private final ReactiveRedisOperations<String, Stop> stopRedisOps;
    private final ReactiveRedisOperations<String, Integer> intRedisOps;


    @Autowired
    public LineRepositoryRedisImpl(ReactiveRedisOperations<String, Line> lineRedisOps,
            ReactiveRedisOperations<String, Stop> stopRedisOps,
            ReactiveRedisOperations<String, Integer> intRedisOps) {
        this.lineRedisOps = lineRedisOps;
        this.stopRedisOps = stopRedisOps;
        this.intRedisOps = intRedisOps;
    }

    @Override
    public Mono<Line> create(Line line) {
        var key = getLineEntityKey(line.getLineNumber());

        return deleteAllStopsOfLine(line.getLineNumber())
                .then(lineRedisOps.opsForValue()
                        .set(key, line)
                        .flatMap(success -> {
                            if (success) {
                                return Mono.just(line);
                            } else {
                                return Mono.error(new EntityCreationFailureException("Failed to save line to Redis."));
                            }
                        }));

    }

    @Override
    public Mono<Line> addStopToLine(Line line, Stop stop) {
        var lineStopsKey = getLineStopsKey(line.getLineNumber());
        var lineStopCountKey = getLineStopCountKey(line.getLineNumber());

        return stopRedisOps.opsForList().rightPush(lineStopsKey, stop)
                .then(intRedisOps.opsForValue().increment(lineStopCountKey))
                .then(Mono.just(line));
    }

    @Override
    public Mono<Line> findByLineNumber(String lineNumber) {
        var key = getLineEntityKey(lineNumber);
        return lineRedisOps.opsForValue().get(key);
    }

    @Override
    public Flux<Line> findAll() {
        var key = getLineEntityKey("*");
        return lineRedisOps.keys(key)
                .flatMap(lineRedisOps.opsForValue()::get);
    }

    @Override
    public Mono<Long> deleteAll() {
        var key = getLineEntityKey("*");
        return deleteAllStopsOfLine("*")
                .then(lineRedisOps.delete(lineRedisOps.keys(key)));
    }

    private Mono<Long> deleteAllStopsOfLine(String lineNumber) {
        var lineStopsKey = getLineStopsKey(lineNumber);
        var lineStopCountKey = getLineStopCountKey(lineNumber);
        return stopRedisOps.delete(lineStopsKey)
                .then(intRedisOps.delete(lineStopCountKey));
    }

    public Mono<List<Stop>> fetchStopsOfLine(String lineNumber) {
        var lineStopsKey = getLineStopsKey(lineNumber);
        return stopRedisOps.opsForList()
                .range(lineStopsKey, 0, -1)
                .collectList();
    }

    public Mono<Integer> fetchStopCountOfLine(String lineNumber) {
        var lineStopCountKey = getLineStopCountKey(lineNumber);
        return intRedisOps.opsForValue().get(lineStopCountKey);
    }

    private String getLineEntityKey(String lineNumber) {
        return LINE_ENTITY_KEY_PREFIX.formatted(lineNumber);
    }

    private String getLineStopsKey(String lineNumber) {
        return LINE_STOPS_KEY.formatted(lineNumber);
    }

    private String getLineStopCountKey(String lineNumber) {
        return LINE_STOP_COUNT_KEY.formatted(lineNumber);
    }

}