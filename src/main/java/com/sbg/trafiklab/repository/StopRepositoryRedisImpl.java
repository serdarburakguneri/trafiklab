package com.sbg.trafiklab.repository;

import com.sbg.trafiklab.entity.Stop;
import com.sbg.trafiklab.exception.EntityCreationFailureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class StopRepositoryRedisImpl implements StopRepository {

    private static final String STOP_ENTITY_KEY_PREFIX = "stop:%s";

    private final ReactiveRedisOperations<String, Stop> stopRedisOps;

    @Autowired
    public StopRepositoryRedisImpl(ReactiveRedisOperations<String, Stop> stopRedisOps) {
        this.stopRedisOps = stopRedisOps;
    }

    @Override
    public Mono<Stop> create(Stop stop) {
        var key = getStopEntityKey(stop.getStopNumber());

        return stopRedisOps.opsForValue()
                .set(key, stop)
                .flatMap(success -> {
                    if (success) {
                        return Mono.just(stop);
                    } else {
                        return Mono.error(new EntityCreationFailureException("Failed to save stop to Redis."));
                    }
                });
    }

    @Override
    public Mono<Stop> findByStopNumber(String stopNumber) {
        var key = getStopEntityKey(stopNumber);
        return stopRedisOps.opsForValue().get(key);
    }

    @Override
    public Flux<Stop> findAll() {
        var key = getStopEntityKey("*");
        return stopRedisOps.keys(key)
                .flatMap(stopRedisOps.opsForValue()::get);
    }

    @Override
    public Mono<Long> deleteAll() {
        var key = getStopEntityKey("*");
        return stopRedisOps.delete(stopRedisOps.keys(key));
    }

    private String getStopEntityKey(String stopNumber) {
        return STOP_ENTITY_KEY_PREFIX.formatted(stopNumber);
    }
}
