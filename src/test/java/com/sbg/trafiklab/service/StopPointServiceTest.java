package com.sbg.trafiklab.service;

import static org.mockito.Mockito.*;

import com.sbg.trafiklab.entity.StopPoint;
import com.sbg.trafiklab.repository.StopPointRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class StopPointServiceTest {

    @Mock
    private StopPointRepository stopPointRepository;

    @InjectMocks
    private StopPointService stopPointService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveNewStopPoint() {
        var newStopPoint = new StopPoint();
        newStopPoint.setStopPointNumber("123");

        when(stopPointRepository.findByStopPointNumber("123")).thenReturn(Mono.empty());
        when(stopPointRepository.save(newStopPoint)).thenReturn(Mono.just(newStopPoint));

        var result = stopPointService.save(newStopPoint);

        StepVerifier.create(result)
                .expectNext(newStopPoint)
                .verifyComplete();

        verify(stopPointRepository, times(1)).save(newStopPoint);
    }

    @Test
    public void testSaveExistingStopPoint() {
        var existingStopPoint = new StopPoint();
        existingStopPoint.setStopPointNumber("123");

        when(stopPointRepository.findByStopPointNumber("123")).thenReturn(Mono.just(existingStopPoint));

        var result = stopPointService.save(existingStopPoint);

        StepVerifier.create(result)
                .expectNext(existingStopPoint)
                .verifyComplete();

        verify(stopPointRepository, never()).save(any(StopPoint.class));
    }

    @Test
    public void testSaveThrowsException() {
        var newStopPoint = new StopPoint();
        newStopPoint.setStopPointNumber("123");
        var exception = new RuntimeException("Database error");

        when(stopPointRepository.findByStopPointNumber("123")).thenReturn(Mono.empty());
        when(stopPointRepository.save(newStopPoint)).thenReturn(Mono.error(exception));

        var result = stopPointService.save(newStopPoint);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException
                        && throwable.getMessage().equals("An error occurred while saving stop point."))
                .verify();

        verify(stopPointRepository, times(1)).save(newStopPoint);
    }
}
