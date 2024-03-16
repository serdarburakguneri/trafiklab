package com.sbg.trafiklab.datasyncer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sbg.trafiklab.integration.dto.SLLineData;
import com.sbg.trafiklab.mapper.LineMapper;
import com.sbg.trafiklab.entity.Line;
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
public class LineDataSyncer extends AbstractSLDataSyncer<Line> {

    @Value("${trafiklab.data.line.filepath}")
    private String filePath;

    private final ObjectMapper objectMapper;
    private final LineService lineService;
    private static final Logger logger = LoggerFactory.getLogger(LineDataSyncer.class);

    @Autowired
    public LineDataSyncer(ObjectMapper objectMapper,
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
    protected Line convertJsonNodeToEntity(JsonParser parser) throws IOException {
        var lineNode = objectMapper.readValue(parser, SLLineData.class);
        return LineMapper.fromSLLineData(lineNode);
    }

    @Override
    protected Mono<Void> saveEntity(Line entity) {
        return lineService.create(entity)
                .onErrorResume(e -> {
                    logger.error("An error occurred while handling line: {}", e.getMessage(), e);
                    return Mono.empty(); // Let's not stop the whole process if one entity fails to save
                })
                .then(Mono.empty());
    }
}

