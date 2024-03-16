package com.sbg.trafiklab.controller;

import com.sbg.trafiklab.dto.LineDTO;
import com.sbg.trafiklab.mapper.LineDTOMapper;
import com.sbg.trafiklab.service.LineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/line")
public class LineController {

    private final LineService lineService;

    @Autowired
    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @GetMapping
    public Flux<LineDTO> findAll(@RequestParam(defaultValue = "10") int limit) {
        return lineService.findAll(limit).map(LineDTOMapper::fromLine);
    }

    @GetMapping
    @RequestMapping("/{lineNumber}")
    public Mono<LineDTO> findByLineNumber(@PathVariable String lineNumber) {
        return lineService.findByLineNumber(lineNumber).map(LineDTOMapper::fromLine);
    }
}
