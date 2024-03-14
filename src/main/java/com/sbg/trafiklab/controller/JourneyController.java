package com.sbg.trafiklab.controller;


import com.sbg.trafiklab.dto.JourneyDTO;
import com.sbg.trafiklab.service.JourneyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;


@RestController
@RequestMapping("/journey")
public class JourneyController {

    private final JourneyService journeyService;

    @Autowired
    public JourneyController(JourneyService journeyService) {
        this.journeyService = journeyService;
    }


    @GetMapping
    public Flux<JourneyDTO> findAll(@RequestParam(defaultValue = "10") int limit) {
        return journeyService.findAll(limit);
    }

}
