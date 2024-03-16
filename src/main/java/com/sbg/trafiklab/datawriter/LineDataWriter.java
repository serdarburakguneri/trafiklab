package com.sbg.trafiklab.datawriter;

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
public class LineDataWriter extends AbstractSLDataWriter {

    @Value("${trafiklab.data.line.filepath}")
    private String filePath;

    private final SLApiClient slApiClient;

    @Autowired
    public LineDataWriter(ResourceLoader resourceLoader, SLApiClient slApiClient) {
        super(resourceLoader);
        this.slApiClient = slApiClient;
    }

    @Override
    protected Path getFilePath() {
        return Paths.get(filePath);
    }

    @Override
    protected Flux<DataBuffer> fetchDataFromAPI() {
        return slApiClient.fetchLines().bodyToFlux(DataBuffer.class);
    }

}

