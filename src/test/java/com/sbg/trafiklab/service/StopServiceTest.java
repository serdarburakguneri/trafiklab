package com.sbg.trafiklab.service;

import com.sbg.trafiklab.entity.Stop;
import com.sbg.trafiklab.exception.EntityCreationFailureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.sbg.trafiklab.repository.StopRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

class StopServiceTest {

    private StopService stopService;

    @Mock
    private StopRepository stopRepository;

    private Stop stop;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        stopService = new StopService(stopRepository);
        stop = new Stop();
    }

    @Test
    void testCreateStop() {
        when(stopRepository.create(any(Stop.class))).thenReturn(Mono.just(stop));

        StepVerifier.create(stopService.create(stop))
                .expectNext(stop)
                .verifyComplete();
    }

    @Test
    void testCreateStopWhenRepositoryRaisesException() {
        when(stopRepository.create(any(Stop.class))).thenReturn(Mono.error(new EntityCreationFailureException("Creation failed")));

        StepVerifier.create(stopService.create(stop))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException && throwable.getMessage()
                        .equals("An error occurred while creating stop."))
                .verify();
    }

    @Test
    void testFindAll() {
        Stop stop1 = new Stop();
        Stop stop2 = new Stop();
        when(stopRepository.findAll()).thenReturn(Flux.just(stop1, stop2));

        StepVerifier.create(stopService.findAll(2))
                .expectNext(stop1)
                .expectNext(stop2)
                .verifyComplete();
    }

    @Test
    void deleteAllStopsTest() {
        when(stopRepository.deleteAll()).thenReturn(Mono.just(2L));

        StepVerifier.create(stopService.deleteAll())
                .expectNext(2L)
                .verifyComplete();
    }


}
