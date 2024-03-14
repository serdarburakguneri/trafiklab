package com.sbg.trafiklab.service;

import com.sbg.trafiklab.entity.Line;
import com.sbg.trafiklab.repository.LineRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class LineService {

    private final LineRepository lineRepository;

    private static final Logger logger = LoggerFactory.getLogger(LineService.class);

    @Autowired
    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public Mono<Line> save(Line line) {
        return lineRepository.findByLineNumber(line.getLineNumber())
                .switchIfEmpty(
                        Mono.defer(() -> lineRepository.save(line))
                                .doOnSuccess(
                                        savedLine -> logger.info(
                                                "Line saved. Line number: %s".formatted(savedLine.getLineNumber())))
                                .onErrorResume(e -> {
                                    var message = "An error occurred while saving line.";
                                    logger.error(message, e);
                                    return Mono.error(new RuntimeException(message, e));
                                })
                );
    }

}
