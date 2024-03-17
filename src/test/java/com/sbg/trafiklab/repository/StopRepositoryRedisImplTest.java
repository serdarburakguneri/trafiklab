package com.sbg.trafiklab.repository;

import com.sbg.trafiklab.common.IntegrationTest;
import com.sbg.trafiklab.entity.Stop;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class StopRepositoryRedisImplTest extends IntegrationTest {

    @Autowired
    private StopRepositoryRedisImpl stopRepository;

    @Test
    void testCreateStop() {
        var newStop = new Stop();
        newStop.setStopNumber("123");
        newStop.setStopName("Test Stop");
        stopRepository.create(newStop).block();

        var fetchedStop = stopRepository.findByStopNumber(newStop.getStopNumber()).block();

        assertNotNull(fetchedStop);
        assertEquals(newStop.getStopNumber(), fetchedStop.getStopNumber());
        assertEquals(newStop.getStopName(), fetchedStop.getStopName());
    }

    @Test
    void testFindByStopNumber() {
        var stop = new Stop();
        stop.setStopNumber("123");
        stop.setStopName("Test Stop 1");
        stopRepository.create(stop).block();

        var fetchedStop = stopRepository.findByStopNumber(stop.getStopNumber()).block();

        assertNotNull(fetchedStop);
        assertEquals(stop.getStopNumber(), fetchedStop.getStopNumber());
        assertEquals(stop.getStopName(), fetchedStop.getStopName());
    }

    @Test
    void testFindAllStops() {

        var stop1 = new Stop();
        stop1.setStopNumber("123");
        stop1.setStopName("Test Stop 1");
        stopRepository.create(stop1).block();

        var stop2 = new Stop();
        stop2.setStopNumber("456");
        stop2.setStopName("Test Stop 2");
        stopRepository.create(stop2).block();

        var stops = stopRepository.findAll().collectList().block();

        assertNotNull(stops);
        assertEquals(2, stops.size());
        assertTrue(stops.stream()
                .anyMatch(stop -> stop.getStopNumber().equals(stop1.getStopNumber()) && stop.getStopName()
                        .equals(stop1.getStopName())));
        assertTrue(stops.stream()
                .anyMatch(stop -> stop.getStopNumber().equals(stop2.getStopNumber()) && stop.getStopName()
                        .equals(stop2.getStopName())));
    }

    @Test
    void testDeleteAllStops() {
        var stop1 = new Stop();
        stop1.setStopNumber("123");
        stop1.setStopName("Test Stop 1");
        stopRepository.create(stop1).block();

        var stop2 = new Stop();
        stop2.setStopNumber("456");
        stop2.setStopName("Test Stop 2");
        stopRepository.create(stop2).block();

        stopRepository.deleteAll().block();

        var stops = stopRepository.findAll().collectList().block();

        assertNotNull(stops);
        assertTrue(stops.isEmpty());
    }

}
