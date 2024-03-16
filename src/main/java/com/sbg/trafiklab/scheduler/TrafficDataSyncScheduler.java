package com.sbg.trafiklab.scheduler;

import com.sbg.trafiklab.datasyncer.JourneyPatternDataSyncer;
import com.sbg.trafiklab.datasyncer.LineDataSyncer;
import com.sbg.trafiklab.datasyncer.StopDataSyncer;
import com.sbg.trafiklab.datawriter.JourneyPatternDataWriter;
import com.sbg.trafiklab.datawriter.LineDataWriter;
import com.sbg.trafiklab.datawriter.StopDataWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class TrafficDataSyncScheduler {

    private final LineDataWriter lineDataWriter;
    private final StopDataWriter stopDataWriter;
    private final JourneyPatternDataWriter journeyPatternDataWriter;
    private final LineDataSyncer lineDataSyncer;
    private final StopDataSyncer stopDataSyncer;
    private final JourneyPatternDataSyncer journeyPatternDataSyncer;

    private static final Logger logger = LoggerFactory.getLogger(TrafficDataSyncScheduler.class);

    @Autowired
    public TrafficDataSyncScheduler(LineDataWriter lineDataWriter,
            StopDataWriter stopDataWriter,
            JourneyPatternDataWriter journeyPatternDataWriter,
            LineDataSyncer lineDataSyncer,
            StopDataSyncer stopDataSyncer,
            JourneyPatternDataSyncer journeyPatternDataSyncer) {
        this.lineDataWriter = lineDataWriter;
        this.stopDataWriter = stopDataWriter;
        this.journeyPatternDataWriter = journeyPatternDataWriter;
        this.lineDataSyncer = lineDataSyncer;
        this.stopDataSyncer = stopDataSyncer;
        this.journeyPatternDataSyncer = journeyPatternDataSyncer;
    }

    @Scheduled(cron = "0 0 3 * * ?", zone = "CET")
    public void syncTrafficData() {

        //lineDataWriter.fetchAndWriteData()
        //      .then(stopPointDataWriter.fetchAndWriteData())
        //    .then(journeyPatternDataWriter.fetchAndWriteData())
        lineDataSyncer.syncData()
                .flatMap(success -> {
                    if (!success) {
                        return Mono.error(new RuntimeException("Line data sync failed."));
                    }
                    return stopDataSyncer.syncData();
                })
                .flatMap(success -> {
                    if (!success) {
                        return Mono.error(new RuntimeException("Stop point data sync failed."));
                    }
                    return journeyPatternDataSyncer.syncData();
                })
                .subscribe(
                        success -> {
                            if (success) {
                                System.out.println("All syncs completed successfully");
                            } else {
                                System.out.println("Last sync failed");
                            }
                        },
                        error -> System.err.println("Error occurred: " + error.getMessage())
                );

        //A retry mechanism can be added here for sync failures
    }

}
