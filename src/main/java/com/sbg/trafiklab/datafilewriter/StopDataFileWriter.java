package com.sbg.trafiklab.datafilewriter;

import com.sbg.trafiklab.integration.client.SLApiClient;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class StopDataFileWriter extends AbstractSLDataFileWriter {

    @Value("${trafiklab.data.stop.filepath}")
    private String filePath;
    private final SLApiClient slApiClient;

    @Autowired
    public StopDataFileWriter(ResourceLoader resourceLoader, SLApiClient slApiClient) {
        super(resourceLoader);
        this.slApiClient = slApiClient;
    }

    @Override
    protected Path getFilePath() {
        return Paths.get(filePath);
    }

    @Override
    protected Flux<DataBuffer> fetchDataFromAPI() {
        return slApiClient.fetchStopPoints().bodyToFlux(DataBuffer.class);
    }
}
