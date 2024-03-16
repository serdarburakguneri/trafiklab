package com.sbg.trafiklab.service;

import static org.mockito.Mockito.*;

import com.sbg.trafiklab.entity.Stop;
import com.sbg.trafiklab.repository.StopRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class StopServiceTest {

    @Mock
    private StopRepository stopRepository;

    @InjectMocks
    private StopService stopService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveNewStopPoint() {
        var newStopPoint = new Stop();
        newStopPoint.setStopPointNumber("123");

        when(stopRepository.findByStopNumber("123")).thenReturn(Mono.empty());
        when(stopRepository.save(newStopPoint)).thenReturn(Mono.just(newStopPoint));

        var result = stopService.create(newStopPoint);

        StepVerifier.create(result)
                .expectNext(newStopPoint)
                .verifyComplete();

        verify(stopRepository, times(1)).save(newStopPoint);
    }

    @Test
    public void testSaveExistingStopPoint() {
        var existingStopPoint = new Stop();
        existingStopPoint.setStopPointNumber("123");

        when(stopRepository.findByStopNumber("123")).thenReturn(Mono.just(existingStopPoint));

        var result = stopService.create(existingStopPoint);

        StepVerifier.create(result)
                .expectNext(existingStopPoint)
                .verifyComplete();

        verify(stopRepository, never()).save(any(Stop.class));
    }

    @Test
    public void testSaveThrowsException() {
        var newStopPoint = new Stop();
        newStopPoint.setStopPointNumber("123");
        var exception = new RuntimeException("Database error");

        when(stopRepository.findByStopNumber("123")).thenReturn(Mono.empty());
        when(stopRepository.save(newStopPoint)).thenReturn(Mono.error(exception));

        var result = stopService.create(newStopPoint);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException
                        && throwable.getMessage().equals("An error occurred while saving stop point."))
                .verify();

        verify(stopRepository, times(1)).save(newStopPoint);
    }
}
