package com.sbg.trafiklab.datasyncer;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.nio.file.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ResourceLoader;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public abstract class AbstractSLDataSyncer<T> {

    protected final ResourceLoader resourceLoader;

    private static final Logger logger = LoggerFactory.getLogger(AbstractSLDataSyncer.class);

    protected AbstractSLDataSyncer(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public Mono<Boolean> syncData() {
        logger.info("Syncing data started.");
        return readEntitiesFromJsonFile()
                .flatMap(this::saveEntity)
                .doOnComplete(() -> logger.info("Syncing data completed."))
                .onErrorResume(e -> {
                    logger.error("Error during data sync: {}", e.getMessage(), e);
                    return Mono.error(new RuntimeException("Error during data synchronization", e));
                })
                .then(Mono.just(true))
                .onErrorReturn(false);
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
                    } else if ("Result".equals(fieldName)) {
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

    protected abstract Mono<Void> saveEntity(T entity);
}

