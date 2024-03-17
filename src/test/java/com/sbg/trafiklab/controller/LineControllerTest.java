package com.sbg.trafiklab.controller;


import com.sbg.trafiklab.common.IntegrationTest;
import com.sbg.trafiklab.dto.LineDTO;
import com.sbg.trafiklab.dto.StopDTO;
import com.sbg.trafiklab.util.DatabaseTestUtil;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class LineControllerTest extends IntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private DatabaseTestUtil databaseTestUtil;

    @Test
    void testFindAll() {

        var longestLine = new LineDTO("3", 4, List.of(
                new StopDTO("1", "Stop 1"),
                new StopDTO("2", "Stop 2"),
                new StopDTO("3", "Stop 3"),
                new StopDTO("4", "Stop 4"))
        );

        var shortestLine = new LineDTO("2", 2, List.of(
                new StopDTO("1", "Stop 1"),
                new StopDTO("2", "Stop 2")
        ));

        var mediumLine = new LineDTO("1", 3, List.of(
                new StopDTO("1", "Stop 1"),
                new StopDTO("2", "Stop 2"),
                new StopDTO("3", "Stop 3")
        ));

        var lines = List.of(
                mediumLine,
                shortestLine,
                longestLine);

        databaseTestUtil.createDataSet(lines);

        webTestClient.get().uri("/line?limit=" + lines.size())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(LineDTO.class)
                .hasSize(lines.size())
                .consumeWith(response -> {
                    var responseBody = response.getResponseBody();

                    assertNotNull(responseBody);

                    assertNotNull(responseBody.get(0));
                    assertEquals(longestLine.lineNumber(), responseBody.get(0).lineNumber());
                    assertNotNull(responseBody.get(0).stops());
                    assertEquals(longestLine.stops().size(), responseBody.get(0).stops().size());

                    assertNotNull(responseBody.get(1));
                    assertEquals(mediumLine.lineNumber(), responseBody.get(1).lineNumber());
                    assertNotNull(responseBody.get(1).stops());
                    assertEquals(mediumLine.stops().size(), responseBody.get(1).stops().size());

                    assertNotNull(responseBody.get(2));
                    assertEquals(shortestLine.lineNumber(), responseBody.get(2).lineNumber());
                    assertNotNull(responseBody.get(2).stops());
                    assertEquals(shortestLine.stops().size(), responseBody.get(2).stops().size());
                });

        webTestClient.get().uri("/line?limit=" + (lines.size() - 1))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(LineDTO.class)
                .hasSize(lines.size() - 1)
                .consumeWith(response -> {
                    var responseBody = response.getResponseBody();

                    assertNotNull(responseBody);

                    assertNotNull(responseBody.get(0));
                    assertEquals(longestLine.lineNumber(), responseBody.get(0).lineNumber());
                    assertNotNull(responseBody.get(0).stops());
                    assertEquals(longestLine.stops().size(), responseBody.get(0).stops().size());

                    assertNotNull(responseBody.get(1));
                    assertEquals(mediumLine.lineNumber(), responseBody.get(1).lineNumber());
                    assertNotNull(responseBody.get(1).stops());
                    assertEquals(mediumLine.stops().size(), responseBody.get(1).stops().size());

                });
    }

    @Test
    void testFindByLineNumber() {
        var line = new LineDTO("1", 3, List.of(
                new StopDTO("1", "Stop 1"),
                new StopDTO("2", "Stop 2"),
                new StopDTO("3", "Stop 3")
        ));

        databaseTestUtil.createDataSet(List.of(line));

        webTestClient.get().uri("/line/" + line.lineNumber())
                .exchange()
                .expectStatus().isOk()
                .expectBody(LineDTO.class)
                .consumeWith(response -> {
                    var responseBody = response.getResponseBody();

                    assertNotNull(responseBody);
                    assertEquals(line.lineNumber(), responseBody.lineNumber());
                    assertNotNull(responseBody.stops());
                    assertEquals(line.stops().size(), responseBody.stops().size());
                });
    }

}

