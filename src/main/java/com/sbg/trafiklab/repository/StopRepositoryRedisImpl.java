package com.sbg.trafiklab.repository;

import com.sbg.trafiklab.entity.Stop;
import com.sbg.trafiklab.exception.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class StopRepositoryRedisImpl implements StopRepository {

    private static final String STOP_ENTITY_KEY_PREFIX = "stop:";

    private final ReactiveRedisOperations<String, Stop> stopRedisOps;

    private static final Logger logger = LoggerFactory.getLogger(StopRepositoryRedisImpl.class);

    @Autowired
    public StopRepositoryRedisImpl(ReactiveRedisOperations<String, Stop> stopRedisOps) {
        this.stopRedisOps = stopRedisOps;
    }

    @Override
    public Mono<Stop> save(Stop stop) {
        var key = STOP_ENTITY_KEY_PREFIX + stop.getStopPointNumber();

        return stopRedisOps.opsForValue()
                .set(key, stop)
                .flatMap(success -> {
                    if (success) {
                        return Mono.just(stop);
                    } else {
                        return Mono.error(new RuntimeException("Failed to save stop point to Redis"));
                    }
                })
                .doOnSuccess(
                        savedStop -> logger.info("Saved stop point with number: {}", stop.getStopPointNumber()))
                .doOnError(e -> logger.error("Error saving stop point with number: {}", stop.getStopPointNumber(),
                        e));
    }

    @Override
    public Mono<Stop> findByStopNumber(String stopNumber) {
        return stopRedisOps.opsForValue().get(STOP_ENTITY_KEY_PREFIX + stopNumber)
                .switchIfEmpty(
                        Mono.error(new EntityNotFoundException("Stop point not found for number: " + stopNumber)));
    }

    @Override
    public Flux<Stop> findAll() {
        return stopRedisOps.keys(STOP_ENTITY_KEY_PREFIX + "*")
                .flatMap(stopRedisOps.opsForValue()::get);
    }

    @Override
    public Mono<Long> deleteAll() {
        return stopRedisOps.delete(stopRedisOps.keys(STOP_ENTITY_KEY_PREFIX + "*"));
    }
}
