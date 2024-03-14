package com.sbg.trafiklab.datasync;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.sbg.trafiklab.util.FileUtil;
import java.io.IOException;
import java.nio.file.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public abstract class AbstractSLDataSyncer<T> {

    protected final ResourceLoader resourceLoader;

    private static final Logger logger = LoggerFactory.getLogger(AbstractSLDataSyncer.class);

    protected AbstractSLDataSyncer(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Transactional
    public Mono<Void> syncData() {
        logger.info("Syncing data started.");
        return saveApiDataToJsonFile()
                .thenMany(readEntitiesFromJsonFile())
                .flatMap(this::saveEntity)
                .doOnComplete(() -> logger.info("Syncing data completed."))
                .then(Mono.empty());
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

    protected Mono<Void> saveApiDataToJsonFile() {
        return createJsonFile()
                .flatMap(p -> {
                    var data = fetchDataFromAPI();
                    return DataBufferUtils.write(data, p);
                });
    }

    protected Flux<T> readEntitiesFromJsonFile() {
        return Flux.create(fluxSink -> {
            var path = getFilePath();
            var uri = path.toUri().toString();
            try (var parser = new JsonFactory().createParser(resourceLoader.getResource(uri).getInputStream())) {

                if (parser.nextToken() != JsonToken.START_OBJECT) {
                    logger.error("File content format error: Expected START_OBJECT at the beginning of the JSON file.");
                    fluxSink.error(
                            new IllegalStateException("Expected START_OBJECT at the beginning of the JSON file."));
                    return;
                }

                while (parser.nextToken() != JsonToken.END_OBJECT) {
                    var fieldName = parser.getCurrentName();

                    if ("StatusCode".equals(fieldName)) {
                        parser.nextToken();
                        if (parser.getIntValue() != 0) {
                            var errorMessage = ("Fetching traffic data failed due to an Api error. "
                                    + "Please check file at [%s] for more details.").formatted(uri);
                            logger.error(errorMessage);
                            fluxSink.error(new IllegalStateException(errorMessage));
                            return;
                        }
                    }

                    if ("Result".equals(fieldName)) {
                        parser.nextToken();
                        while (parser.nextToken() != JsonToken.END_ARRAY) {
                            fluxSink.next(convertJsonNodeToEntity(parser));
                        }
                    }
                }
                fluxSink.complete();
            } catch (IOException e) {
                logger.error("IOException occurred while reading json file: {}", uri, e);
                fluxSink.error(new IllegalStateException("Error occurred whiled reading json file.", e));
            } catch (Exception e) {
                logger.error("Unexpected exception occurred while reading json file: {}.", uri, e);
                fluxSink.error(new IllegalStateException("Unexpected occurred whiled reading json file.", e));
            }
        });
    }


    protected abstract Path getFilePath();

    protected abstract T convertJsonNodeToEntity(JsonParser parser) throws IOException;

    protected abstract Flux<DataBuffer> fetchDataFromAPI();

    protected abstract Mono<T> saveEntity(T entity);
}

