package com.sbg.trafiklab.scheduler;

import static org.mockito.Mockito.*;

import com.sbg.trafiklab.datasyncer.JourneyPatternDataSyncer;
import com.sbg.trafiklab.datasyncer.LineDataSyncer;
import com.sbg.trafiklab.datasyncer.StopDataSyncer;
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
    private StopDataSyncer stopDataSyncer;

    @Mock
    private JourneyPatternDataSyncer journeyPatternDataSyncer;

    @InjectMocks
    private TrafficDataSyncScheduler trafficDataSyncScheduler;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        when(lineDataSyncer.syncData()).thenReturn(Mono.empty());
        when(stopDataSyncer.syncData()).thenReturn(Mono.empty());
        when(journeyPatternDataSyncer.syncData()).thenReturn(Mono.empty());
    }

    @Test
    public void testSyncTrafficData_CallsAllSyncMethods() {
        trafficDataSyncScheduler.syncTrafficData();

        verify(lineDataSyncer, times(1)).syncData();
        verify(stopDataSyncer, times(1)).syncData();
        verify(journeyPatternDataSyncer, times(1)).syncData();
    }

}

