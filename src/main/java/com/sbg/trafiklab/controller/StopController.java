package com.sbg.trafiklab.controller;

import com.sbg.trafiklab.dto.StopDTO;
import com.sbg.trafiklab.mapper.StopDTOMapper;
import com.sbg.trafiklab.service.StopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/stop")
public class StopController {

    private final StopService stopService;

    @Autowired
    public StopController(StopService stopService) {
        this.stopService = stopService;
    }

    @GetMapping
    public Flux<StopDTO> findAll(@RequestParam(defaultValue = "10") int limit) {
        return stopService.findAll(limit).map(StopDTOMapper::fromStop);
    }

}
