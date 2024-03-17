package com.sbg.trafiklab.datafilewriter;

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


public abstract class AbstractSLDataFileWriter {

    protected final ResourceLoader resourceLoader;

    private static final Logger logger = LoggerFactory.getLogger(AbstractSLDataFileWriter.class);

    protected AbstractSLDataFileWriter(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public Mono<Void> fetchDataAndWriteToFile() {
        return createJsonFile()
                .doOnSuccess(p -> logger.info("Fetching and writing data started."))
                .flatMap(p -> {
                    var data = fetchDataFromAPI();
                    return DataBufferUtils.write(data, p)
                            .doOnSuccess(v -> logger.info("Fetching and writing data completed."))
                            .onErrorResume(e -> {
                                var message = "An error occurred while fetching or writing data.";
                                logger.error(message, e.getMessage(), e);
                                return Mono.error(new IllegalStateException(message, e));
                            });
                });
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

