package com.sbg.trafiklab.scheduler;

import com.sbg.trafiklab.datasyncer.JourneyPatternDataSyncer;
import com.sbg.trafiklab.datasyncer.LineDataSyncer;
import com.sbg.trafiklab.datasyncer.StopDataSyncer;
import com.sbg.trafiklab.datafilewriter.JourneyPatternDataFileWriter;
import com.sbg.trafiklab.datafilewriter.LineDataFileWriter;
import com.sbg.trafiklab.datafilewriter.StopDataFileWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class TrafficDataSyncScheduler {

    @Value("${trafiklab.data.fetch-from-api}")
    private boolean fetchFromAPI;

    private final LineDataFileWriter lineDataWriter;
    private final StopDataFileWriter stopDataWriter;
    private final JourneyPatternDataFileWriter journeyPatternDataWriter;
    private final LineDataSyncer lineDataSyncer;
    private final StopDataSyncer stopDataSyncer;
    private final JourneyPatternDataSyncer journeyPatternDataSyncer;

    private static final Logger logger = LoggerFactory.getLogger(TrafficDataSyncScheduler.class);

    @Autowired
    public TrafficDataSyncScheduler(LineDataFileWriter lineDataWriter,
            StopDataFileWriter stopDataWriter,
            JourneyPatternDataFileWriter journeyPatternDataWriter,
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

        if (!fetchFromAPI) {
            logger.info("Traffic data fetch from API is disabled by configuration. Fetching data from local files.");
        }

        var flow = fetchFromAPI ? fetchDataFromApi().then(syncData()) : syncData();

        flow.subscribe(
                success -> logger.info("Traffic data fetch and sync process completed successfully."),
                error -> logger.error("Traffic data fetch and sync process failed due to error: ", error));
    }

    private Mono<Void> fetchDataFromApi() {
        return lineDataWriter.fetchDataAndWriteToFile()
                .then(stopDataWriter.fetchDataAndWriteToFile())
                .then(journeyPatternDataWriter.fetchDataAndWriteToFile());
    }

    private Mono<Void> syncData() {
        return lineDataSyncer.syncData()
                .then(stopDataSyncer.syncData())
                .then(journeyPatternDataSyncer.syncData());
    }

}
