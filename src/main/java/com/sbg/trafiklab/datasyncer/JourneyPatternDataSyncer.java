package com.sbg.trafiklab.datasyncer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sbg.trafiklab.integration.dto.SLJourneyPattern;
import com.sbg.trafiklab.mapper.JourneyPatternMapper;
import com.sbg.trafiklab.entity.JourneyPattern;
import com.sbg.trafiklab.service.LineService;
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
public class JourneyPatternDataSyncer extends AbstractSLDataSyncer<JourneyPattern> {

    @Value("${trafiklab.data.journey.filepath}")
    private String filePath;
    private final ObjectMapper objectMapper;
    private final LineService lineService;

    private static final Logger logger = LoggerFactory.getLogger(JourneyPatternDataSyncer.class);

    @Autowired
    public JourneyPatternDataSyncer(ObjectMapper objectMapper,
            ResourceLoader resourceLoader,
            LineService lineService) {
        super(resourceLoader);
        this.objectMapper = objectMapper;
        this.lineService = lineService;
    }

    @Override
    protected Path getFilePath() {
        return Paths.get(filePath);
    }

    @Override
    protected JourneyPattern convertJsonNodeToEntity(JsonParser parser) throws IOException {
        var journeyPatternNode = objectMapper.readValue(parser, SLJourneyPattern.class);
        return JourneyPatternMapper.fromSLJourneyPatter(journeyPatternNode);
    }

    @Override
    protected Mono<Void> saveEntity(JourneyPattern entity) {
        return lineService.addStopToLine(entity.getLineNumber(), entity.getStopPointNumber())
                .onErrorResume(e -> {
                    logger.error("An error occurred while handling journey pattern: {}", e.getMessage(), e);
                    return Mono.empty(); // Let's not stop the whole process if one entity fails to save
                })
                .then(Mono.empty());
    }

}

