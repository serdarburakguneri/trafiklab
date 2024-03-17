package com.sbg.trafiklab.datasyncer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sbg.trafiklab.entity.Stop;
import com.sbg.trafiklab.integration.dto.SLStopPoint;
import com.sbg.trafiklab.mapper.StopMapper;
import com.sbg.trafiklab.service.StopService;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class StopDataSyncer extends AbstractSLDataSyncer<Stop> {

    @Value("${trafiklab.data.stop.filepath}")
    private String filePath;
    private final StopService stopService;
    private final ObjectMapper objectMapper;

    private static final Logger logger = LoggerFactory.getLogger(StopDataSyncer.class);

    @Autowired
    public StopDataSyncer(ObjectMapper objectMapper,
            ResourceLoader resourceLoader,
            StopService stopService) {
        super(resourceLoader);
        this.objectMapper = objectMapper;
        this.stopService = stopService;
    }

    @Override
    protected Path getFilePath() {
        return Paths.get(filePath);
    }

    @Override
    protected Stop convertJsonNodeToEntity(JsonParser parser) throws IOException {
        var stopPointNode = objectMapper.readValue(parser, SLStopPoint.class);
        return StopMapper.fromSLStopPoint(stopPointNode);
    }

    @Override
    protected Mono<Void> saveEntity(Stop entity) {
        return stopService.create(entity)
                .onErrorResume(e -> {
                    logger.warn("An error occurred while handling stop point: {}", e.getMessage(), e);
                    return Mono.empty(); // Let's not stop the whole process if one entity fails to save
                })
                .then(Mono.empty());
    }
}
