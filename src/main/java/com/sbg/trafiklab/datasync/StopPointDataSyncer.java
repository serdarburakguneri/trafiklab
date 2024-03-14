package com.sbg.trafiklab.datasync;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sbg.trafiklab.entity.StopPoint;
import com.sbg.trafiklab.integration.dto.SLStopPoint;
import com.sbg.trafiklab.integration.client.SLApiClient;
import com.sbg.trafiklab.mapper.StopPointMapper;
import com.sbg.trafiklab.service.StopPointService;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class StopPointDataSyncer extends AbstractSLDataSyncer<StopPoint> {

    @Value("${trafiklab.data.stop.filepath}")
    private String filePath;
    private final StopPointService stopPointService;
    private final SLApiClient slApiClient;
    private final ObjectMapper objectMapper;

    @Autowired
    public StopPointDataSyncer(ObjectMapper objectMapper,
            ResourceLoader resourceLoader,
            StopPointService stopPointService,
            SLApiClient slApiClient) {
        super(resourceLoader);
        this.objectMapper = objectMapper;
        this.stopPointService = stopPointService;
        this.slApiClient = slApiClient;
    }

    @Override
    protected Path getFilePath() {
        return Paths.get(filePath);
    }

    @Override
    protected StopPoint convertJsonNodeToEntity(JsonParser parser) throws IOException {
        var stopPointNode = objectMapper.readValue(parser, SLStopPoint.class);
        return StopPointMapper.fromSLStopPoint(stopPointNode);
    }

    @Override
    protected Flux<DataBuffer> fetchDataFromAPI() {
        return slApiClient.fetchStopPoints().bodyToFlux(DataBuffer.class);
    }

    @Override
    protected Mono<StopPoint> saveEntity(StopPoint entity) {
        return stopPointService.save(entity);
    }
}
