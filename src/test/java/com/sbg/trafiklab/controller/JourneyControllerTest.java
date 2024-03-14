package com.sbg.trafiklab.controller;

import com.sbg.trafiklab.common.IntegrationTest;
import com.sbg.trafiklab.dto.JourneyDTO;
import com.sbg.trafiklab.dto.StopPointDTO;
import com.sbg.trafiklab.util.DatabaseTestUtil;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class JourneyControllerTest extends IntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private DatabaseTestUtil databaseTestUtil;

    @Test
    void testJourneyEndpointReturnsCorrectData() {

        var longestJourney = new JourneyDTO("3", List.of(
                new StopPointDTO("1", "Stop 1"),
                new StopPointDTO("2", "Stop 2"),
                new StopPointDTO("3", "Stop 3"),
                new StopPointDTO("4", "Stop 4"))
        );

        var shortestJourney = new JourneyDTO("2", List.of(
                new StopPointDTO("1", "Stop 1"),
                new StopPointDTO("2", "Stop 2")
        ));

        var mediumJourney = new JourneyDTO("1", List.of(
                new StopPointDTO("1", "Stop 1"),
                new StopPointDTO("2", "Stop 2"),
                new StopPointDTO("3", "Stop 3")
        ));

        var journeys = List.of(
                mediumJourney,
                shortestJourney,
                longestJourney);

        databaseTestUtil.createDataSet(journeys);

        webTestClient.get().uri("/journey?limit=" + journeys.size())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(JourneyDTO.class)
                .hasSize(journeys.size())
                .consumeWith(response -> {
                    var responseBody = response.getResponseBody();

                    assertNotNull(responseBody);

                    assertNotNull(responseBody.get(0));
                    assertEquals(longestJourney.lineNumber(), responseBody.get(0).lineNumber());
                    assertNotNull(responseBody.get(0).stops());
                    assertEquals(longestJourney.stops().size(), responseBody.get(0).stops().size());

                    assertNotNull(responseBody.get(1));
                    assertEquals(mediumJourney.lineNumber(), responseBody.get(1).lineNumber());
                    assertNotNull(responseBody.get(1).stops());
                    assertEquals(mediumJourney.stops().size(), responseBody.get(1).stops().size());

                    assertNotNull(responseBody.get(2));
                    assertEquals(shortestJourney.lineNumber(), responseBody.get(2).lineNumber());
                    assertNotNull(responseBody.get(2).stops());
                    assertEquals(shortestJourney.stops().size(), responseBody.get(2).stops().size());
                });

        webTestClient.get().uri("/journey?limit=" + (journeys.size() - 1))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(JourneyDTO.class)
                .hasSize(journeys.size() - 1)
                .consumeWith(response -> {
                    var responseBody = response.getResponseBody();

                    assertNotNull(responseBody);

                    assertNotNull(responseBody.get(0));
                    assertEquals(longestJourney.lineNumber(), responseBody.get(0).lineNumber());
                    assertNotNull(responseBody.get(0).stops());
                    assertEquals(longestJourney.stops().size(), responseBody.get(0).stops().size());

                    assertNotNull(responseBody.get(1));
                    assertEquals(mediumJourney.lineNumber(), responseBody.get(1).lineNumber());
                    assertNotNull(responseBody.get(1).stops());
                    assertEquals(mediumJourney.stops().size(), responseBody.get(1).stops().size());

                });

    }

}

