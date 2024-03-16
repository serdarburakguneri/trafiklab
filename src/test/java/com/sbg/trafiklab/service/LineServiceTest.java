package com.sbg.trafiklab.service;

import static org.mockito.Mockito.*;

import com.sbg.trafiklab.entity.Line;
import com.sbg.trafiklab.repository.LineRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class LineServiceTest {

    @Mock
    private LineRepository lineRepository;

    @InjectMocks
    private LineService lineService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveNewLine() {
        var newLine = new Line();
        newLine.setLineNumber("100");

        when(lineRepository.findByLineNumber("100")).thenReturn(Mono.empty());
        when(lineRepository.save(newLine)).thenReturn(Mono.just(newLine));

        var result = lineService.create(newLine);

        StepVerifier.create(result)
                .expectNext(newLine)
                .verifyComplete();

        verify(lineRepository, times(1)).save(newLine);
    }

    @Test
    public void testSaveExistingLine() {
        var existingLine = new Line();
        existingLine.setLineNumber("100");

        when(lineRepository.findByLineNumber("100")).thenReturn(Mono.just(existingLine));

        var result = lineService.create(existingLine);

        StepVerifier.create(result)
                .expectNext(existingLine)
                .verifyComplete();

        verify(lineRepository, never()).save(any(Line.class));
    }

    @Test
    public void testSaveThrowsException() {
        var newLine = new Line();
        newLine.setLineNumber("100");
        var exception = new RuntimeException("Database error");

        when(lineRepository.findByLineNumber("100")).thenReturn(Mono.empty());
        when(lineRepository.save(newLine)).thenReturn(Mono.error(exception));

        var result = lineService.create(newLine);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException
                        && throwable.getMessage().equals("An error occurred while saving line."))
                .verify();

        verify(lineRepository, times(1)).save(newLine);
    }

}