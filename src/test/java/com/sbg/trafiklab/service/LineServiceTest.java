package com.sbg.trafiklab.service;

import com.sbg.trafiklab.entity.Line;
import com.sbg.trafiklab.entity.Stop;
import com.sbg.trafiklab.repository.LineRepository;
import com.sbg.trafiklab.repository.StopRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;


import static org.mockito.Mockito.*;

public class LineServiceTest {

    @Mock
    private LineRepository lineRepository;

    @Mock
    private StopRepository stopRepository;

    @InjectMocks
    private LineService lineService;

    private Line line;
    private Stop stop;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        line = new Line();
        stop = new Stop();
    }

    @Test
    void testCreate() {
        when(lineRepository.create(line)).thenReturn(Mono.just(line));
        StepVerifier.create(lineService.create(line))
                .expectNext(line)
                .verifyComplete();
        verify(lineRepository).create(line);
    }

    @Test
    void testCreateStopWhenRepositoryRaisesException() {
        when(lineRepository.create(any(Line.class))).thenReturn(Mono.error(new RuntimeException("Creation failed")));

        StepVerifier.create(lineService.create(line))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException && throwable.getMessage()
                        .equals("An error occurred while creating line."))
                .verify();
    }

    @Test
    void testAddStopToLine() {
        var lineNumber = "123";
        var stopNumber = "456";
        when(lineRepository.findByLineNumber(lineNumber)).thenReturn(Mono.just(line));
        when(stopRepository.findByStopNumber(stopNumber)).thenReturn(Mono.just(stop));
        when(lineRepository.addStopToLine(line, stop)).thenReturn(Mono.just(line));

        StepVerifier.create(lineService.addStopToLine(lineNumber, stopNumber))
                .expectNext(line)
                .verifyComplete();
        verify(lineRepository).findByLineNumber(lineNumber);
        verify(stopRepository).findByStopNumber(stopNumber);
        verify(lineRepository).addStopToLine(line, stop);
    }

    @Test
    void testFindByLineNumber() {
        var lineNumber = "123";
        line.setLineNumber(lineNumber);
        when(lineRepository.findByLineNumber(lineNumber)).thenReturn(Mono.just(line));
        when(lineRepository.fetchStopsOfLine(lineNumber)).thenReturn(Mono.just(List.of(stop)));
        when(lineRepository.fetchStopCountOfLine(lineNumber)).thenReturn(Mono.just(1));

        StepVerifier.create(lineService.findByLineNumber(lineNumber))
                .expectNextMatches(foundLine ->
                        foundLine.getStops().size() == 1 && foundLine.getStopCount() == 1)
                .verifyComplete();
        verify(lineRepository).findByLineNumber(lineNumber);
        verify(lineRepository).fetchStopsOfLine(lineNumber);
        verify(lineRepository).fetchStopCountOfLine(lineNumber);
    }

    @Test
    void testFindAll() {
        var lineNumber = "123";
        line.setLineNumber(lineNumber);
        when(lineRepository.findAll()).thenReturn(Flux.just(line));
        when(lineRepository.fetchStopsOfLine(anyString())).thenReturn(Mono.just(List.of(stop)));
        when(lineRepository.fetchStopCountOfLine(anyString())).thenReturn(Mono.just(1));

        var limit = 1;
        StepVerifier.create(lineService.findAll(limit))
                .expectNextMatches(foundLine ->
                        foundLine.getStops().size() == 1 && foundLine.getStopCount() == 1)
                .verifyComplete();
        verify(lineRepository, times(1)).findAll();
        verify(lineRepository, times(1)).fetchStopsOfLine(anyString());
        verify(lineRepository, times(1)).fetchStopCountOfLine(anyString());
    }

    @Test
    void testDeleteAll() {
        when(lineRepository.deleteAll()).thenReturn(Mono.just(1L));
        StepVerifier.create(lineService.deleteAll())
                .expectNext(1L)
                .verifyComplete();
        verify(lineRepository).deleteAll();
    }
}
