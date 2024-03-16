package com.sbg.trafiklab.datawriter;

import com.sbg.trafiklab.util.FileUtil;
import java.io.IOException;
import java.nio.file.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public abstract class AbstractSLDataWriter {

    protected final ResourceLoader resourceLoader;

    private static final Logger logger = LoggerFactory.getLogger(AbstractSLDataWriter.class);

    protected AbstractSLDataWriter(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public Mono<Void> fetchAndWriteData() {
        logger.info("Fetching and writing data started.");
        return createJsonFile()
                .flatMap(p -> {
                    var data = fetchDataFromAPI();
                    return DataBufferUtils.write(data, p);
                })
                .doOnSuccess(p -> logger.info("Fetching and writing data completed."));
    }

    protected Mono<Path> createJsonFile() {
        return Mono.fromCallable(this::getFilePath)
                .flatMap(path -> Mono.fromCallable(() -> {
                    FileUtil.createFileIfNotPresent(path);
                    return path;
                }))
                .onErrorMap(IOException.class, e -> {
                    logger.error("IOException occurred while creating json file.", e);
                    return new IllegalStateException("Failed to create file", e);
                });
    }

    protected abstract Path getFilePath();

    protected abstract Flux<DataBuffer> fetchDataFromAPI();
}

