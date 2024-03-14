package com.sbg.trafiklab.datasync;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sbg.trafiklab.integration.dto.SLLineData;
import com.sbg.trafiklab.integration.client.SLApiClient;
import com.sbg.trafiklab.mapper.LineMapper;
import com.sbg.trafiklab.entity.Line;
import com.sbg.trafiklab.service.LineService;
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
public class LineDataSyncer extends AbstractSLDataSyncer<Line> {

    @Value("${trafiklab.data.line.filepath}")
    private String filePath;

    private final ObjectMapper objectMapper;
    private final LineService lineService;
    private final SLApiClient slApiClient;

    @Autowired
    public LineDataSyncer(ObjectMapper objectMapper,
            ResourceLoader resourceLoader,
            LineService lineService,
            SLApiClient slApiClient) {
        super(resourceLoader);
        this.objectMapper = objectMapper;
        this.lineService = lineService;
        this.slApiClient = slApiClient;
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
    protected Flux<DataBuffer> fetchDataFromAPI() {
        return slApiClient.fetchLines().bodyToFlux(DataBuffer.class);
    }

    @Override
    protected Mono<Line> saveEntity(Line entity) {
        return lineService.save(entity);
    }
}

