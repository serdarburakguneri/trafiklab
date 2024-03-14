package com.sbg.trafiklab.scheduler;

import com.sbg.trafiklab.datasync.JourneyPatternDataSyncer;
import com.sbg.trafiklab.datasync.LineDataSyncer;
import com.sbg.trafiklab.datasync.StopPointDataSyncer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class TrafficDataSyncScheduler {

    private final LineDataSyncer lineDataSyncer;
    private final StopPointDataSyncer stopPointDataSyncer;
    private final JourneyPatternDataSyncer journeyPatternDataSyncer;

    private static final Logger logger = LoggerFactory.getLogger(TrafficDataSyncScheduler.class);

    @Autowired
    public TrafficDataSyncScheduler(LineDataSyncer lineDataSyncer, StopPointDataSyncer stopPointDataSyncer,
            JourneyPatternDataSyncer journeyPatternDataSyncer) {
        this.lineDataSyncer = lineDataSyncer;
        this.stopPointDataSyncer = stopPointDataSyncer;
        this.journeyPatternDataSyncer = journeyPatternDataSyncer;
    }

    @Scheduled(cron = "0 0 3 * * ?", zone = "CET")
    public void syncTrafficData() {
        lineDataSyncer.syncData()
                .thenMany(stopPointDataSyncer.syncData())
                .thenMany(journeyPatternDataSyncer.syncData())
                .onErrorResume(e -> {
                    logger.error("An error occurred while syncing traffic data.", e);
                    return Mono.empty();
                })
                .subscribe();

        //A retry mechanism can be added here for sync failures
    }

}
