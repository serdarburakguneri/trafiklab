package com.sbg.trafiklab.datasync;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sbg.trafiklab.integration.dto.SLJourneyPattern;
import com.sbg.trafiklab.integration.client.SLApiClient;
import com.sbg.trafiklab.mapper.JourneyPatternMapper;
import com.sbg.trafiklab.entity.JourneyPattern;
import com.sbg.trafiklab.service.JourneyPatternService;
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
public class JourneyPatternDataSyncer extends AbstractSLDataSyncer<JourneyPattern> {

    @Value("${trafiklab.data.journey.filepath}")
    private String filePath;
    private final ObjectMapper objectMapper;
    private final JourneyPatternService journeyPatternService;
    private final SLApiClient slApiClient;

    @Autowired
    public JourneyPatternDataSyncer(ObjectMapper objectMapper,
            ResourceLoader resourceLoader,
            JourneyPatternService journeyPatternService,
            SLApiClient slApiClient) {
        super(resourceLoader);
        this.objectMapper = objectMapper;
        this.journeyPatternService = journeyPatternService;
        this.slApiClient = slApiClient;
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
    protected Flux<DataBuffer> fetchDataFromAPI() {
        return slApiClient.fetchJourneyPatterns().bodyToFlux(DataBuffer.class);
    }

    @Override
    protected Mono<JourneyPattern> saveEntity(JourneyPattern entity) {
        return journeyPatternService.save(entity);
    }
}

