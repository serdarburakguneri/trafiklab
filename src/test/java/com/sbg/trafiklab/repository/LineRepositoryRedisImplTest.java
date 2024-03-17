package com.sbg.trafiklab.repository;

import com.sbg.trafiklab.common.IntegrationTest;
import com.sbg.trafiklab.entity.Line;
import com.sbg.trafiklab.entity.Stop;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LineRepositoryRedisImplTest extends IntegrationTest {

    @Autowired
    private LineRepositoryRedisImpl lineRepository;

    @Autowired
    private StopRepository stopRepository;

    @Test
    void testCreateLine() {
        var newLine = new Line();
        newLine.setLineNumber("123");

        lineRepository.create(newLine).block();

        var fetchedLine = lineRepository.findByLineNumber(newLine.getLineNumber()).block();

        assertNotNull(fetchedLine);
        assertEquals(newLine.getLineNumber(), fetchedLine.getLineNumber());
    }

    @Test
    void testFindByLineNumber() {
        var line = new Line();
        line.setLineNumber("123");
        lineRepository.create(line).block();

        var fetchedLine = lineRepository.findByLineNumber(line.getLineNumber()).block();

        assertNotNull(fetchedLine);
        assertEquals(line.getLineNumber(), fetchedLine.getLineNumber());
    }

    @Test
    void testFindAllLines() {
        var line1 = new Line();
        line1.setLineNumber("123");
        lineRepository.create(line1).block();

        var line2 = new Line();
        line2.setLineNumber("456");
        lineRepository.create(line2).block();

        var lines = lineRepository.findAll().collectList().block();

        assertNotNull(lines);
        assertEquals(2, lines.size());
        assertTrue(lines.stream().anyMatch(line -> line.getLineNumber().equals(line1.getLineNumber())));
        assertTrue(lines.stream().anyMatch(line -> line.getLineNumber().equals(line2.getLineNumber())));
    }

    @Test
    void testAddStopToLine() {
        var line = new Line();
        line.setLineNumber("123");
        lineRepository.create(line).block();

        var stop = new Stop();
        stop.setStopNumber("123");
        stop.setStopName("Test Stop");
        stopRepository.create(stop).block();

        lineRepository.addStopToLine(line, stop).block();

        var fetchedStops = lineRepository.fetchStopsOfLine(line.getLineNumber()).block();

        assertNotNull(fetchedStops);
        assertEquals(1, fetchedStops.size());
        assertTrue(fetchedStops.stream()
                .anyMatch(fetchedStop -> fetchedStop.getStopNumber().equals(stop.getStopNumber())));
    }

    @Test
    void testDeleteAllLines() {
        var line1 = new Line();
        line1.setLineNumber("123");
        lineRepository.create(line1).block();

        var line2 = new Line();
        line2.setLineNumber("456");
        lineRepository.create(line2).block();

        lineRepository.deleteAll().block();

        var lines = lineRepository.findAll().collectList().block();

        assertNotNull(lines);
        assertEquals(0, lines.size());
    }

    @Test
    void testFetchStopCountOfLine() {
        var line = new Line();
        line.setLineNumber("123");
        lineRepository.create(line).block();

        var stop1 = new Stop();
        stop1.setStopNumber("123");
        stop1.setStopName("Test Stop 1");
        stopRepository.create(stop1).block();

        var stop2 = new Stop();
        stop2.setStopNumber("456");
        stop2.setStopName("Test Stop 2");
        stopRepository.create(stop2).block();

        lineRepository.addStopToLine(line, stop1).block();
        lineRepository.addStopToLine(line, stop2).block();

        var stopCount = lineRepository.fetchStopCountOfLine(line.getLineNumber()).block();

        assertNotNull(stopCount);
        assertEquals(2, stopCount);
    }

}
