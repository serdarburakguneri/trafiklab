package com.sbg.trafiklab.service;

import static org.mockito.Mockito.*;

import com.sbg.trafiklab.entity.JourneyPattern;
import com.sbg.trafiklab.repository.JourneyPatternRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class JourneyPatternServiceTest {

    @Mock
    private JourneyPatternRepository journeyPatternRepository;

    @InjectMocks
    private JourneyPatternService journeyPatternService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveNewJourneyPattern() {
        var newJourneyPattern = new JourneyPattern();
        newJourneyPattern.setLineNumber("10");
        newJourneyPattern.setStopPointNumber("100");
        newJourneyPattern.setDirection("1");

        when(journeyPatternRepository.findByLineNumberAndStopPointNumberAndDirection("10", "100", "1"))
                .thenReturn(Mono.empty());
        when(journeyPatternRepository.save(newJourneyPattern)).thenReturn(Mono.just(newJourneyPattern));

        var result = journeyPatternService.save(newJourneyPattern);

        StepVerifier.create(result)
                .expectNext(newJourneyPattern)
                .verifyComplete();

        verify(journeyPatternRepository, times(1)).save(newJourneyPattern);
    }

    @Test
    public void testSaveExistingJourneyPattern() {
        var existingJourneyPattern = new JourneyPattern();
        existingJourneyPattern.setLineNumber("10");
        existingJourneyPattern.setStopPointNumber("100");
        existingJourneyPattern.setDirection("1");

        when(journeyPatternRepository.findByLineNumberAndStopPointNumberAndDirection("10", "100", "1"))
                .thenReturn(Mono.just(existingJourneyPattern));

        var result = journeyPatternService.save(existingJourneyPattern);

        StepVerifier.create(result)
                .expectNext(existingJourneyPattern)
                .verifyComplete();

        verify(journeyPatternRepository, never()).save(any(JourneyPattern.class));
    }

    @Test
    public void testSaveThrowsException() {
        var newJourneyPattern = new JourneyPattern();
        newJourneyPattern.setLineNumber("10");
        newJourneyPattern.setStopPointNumber("100");
        newJourneyPattern.setDirection("2");
        var exception = new RuntimeException("Database error");

        when(journeyPatternRepository.findByLineNumberAndStopPointNumberAndDirection("10", "100", "2"))
                .thenReturn(Mono.empty());
        when(journeyPatternRepository.save(newJourneyPattern)).thenReturn(Mono.error(exception));

        var result = journeyPatternService.save(newJourneyPattern);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException
                        && throwable.getMessage().equals("An error occurred while saving journey pattern."))
                .verify();

        verify(journeyPatternRepository, times(1)).save(newJourneyPattern);
    }

}
