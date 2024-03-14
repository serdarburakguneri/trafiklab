package com.sbg.trafiklab.scheduler;

import static org.mockito.Mockito.*;

import com.sbg.trafiklab.datasync.JourneyPatternDataSyncer;
import com.sbg.trafiklab.datasync.LineDataSyncer;
import com.sbg.trafiklab.datasync.StopPointDataSyncer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;


public class TrafficDataSyncSchedulerTest {

    @Mock
    private LineDataSyncer lineDataSyncer;

    @Mock
    private StopPointDataSyncer stopPointDataSyncer;

    @Mock
    private JourneyPatternDataSyncer journeyPatternDataSyncer;

    @InjectMocks
    private TrafficDataSyncScheduler trafficDataSyncScheduler;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        when(lineDataSyncer.syncData()).thenReturn(Mono.empty());
        when(stopPointDataSyncer.syncData()).thenReturn(Mono.empty());
        when(journeyPatternDataSyncer.syncData()).thenReturn(Mono.empty());
    }

    @Test
    public void testSyncTrafficData_CallsAllSyncMethods() {
        trafficDataSyncScheduler.syncTrafficData();

        verify(lineDataSyncer, times(1)).syncData();
        verify(stopPointDataSyncer, times(1)).syncData();
        verify(journeyPatternDataSyncer, times(1)).syncData();
    }

}

