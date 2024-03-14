package com.sbg.trafiklab.startup;

import com.sbg.trafiklab.scheduler.TrafficDataSyncScheduler;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;


@Component
@ConditionalOnProperty(name = "trafiklab.data.sync-on-start", havingValue = "true")
public class StartupRunner implements CommandLineRunner {

    private final TrafficDataSyncScheduler trafficDataSyncScheduler;

    public StartupRunner(TrafficDataSyncScheduler trafficDataSyncScheduler) {
        this.trafficDataSyncScheduler = trafficDataSyncScheduler;
    }

    @Override
    public void run(String... args) {
        trafficDataSyncScheduler.syncTrafficData();
    }
}

