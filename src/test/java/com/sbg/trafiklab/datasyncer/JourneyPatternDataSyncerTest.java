package com.sbg.trafiklab.datasyncer;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sbg.trafiklab.integration.dto.SLJourneyPattern;
import com.sbg.trafiklab.service.LineService;
import com.sbg.trafiklab.service.StopService;
import java.io.IOException;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ResourceLoader;

class JourneyPatternDataSyncerTest {

    private JourneyPatternDataSyncer journeyPatternDataSyncer;
    private ObjectMapper mockObjectMapper;
    private ResourceLoader mockResourceLoader;
    private LineService mockLineService;
    private StopService mockStopService;

    @BeforeEach
    void setUp() {
        mockObjectMapper = mock(ObjectMapper.class);
        mockResourceLoader = mock(ResourceLoader.class);
        mockLineService = mock(LineService.class);
        mockStopService = mock(StopService.class);
        journeyPatternDataSyncer = new JourneyPatternDataSyncer(mockObjectMapper,
                mockResourceLoader,
                mockLineService,
                mockStopService);
    }

    @Test
    void testConvertJsonNode() throws IOException {
        var mockJsonParser = mock(JsonParser.class);
        var lineNumber = 1;
        var pointNumber = 1;
        var lastModified = LocalDate.now().minusDays(1);
        var sampleData = new SLJourneyPattern(lineNumber, 1, pointNumber, lastModified, lastModified);
        when(mockObjectMapper.readValue(mockJsonParser, SLJourneyPattern.class)).thenReturn(sampleData);

        var result = journeyPatternDataSyncer.convertJsonNodeToEntity(mockJsonParser);

        verify(mockObjectMapper, times(1)).readValue(mockJsonParser, SLJourneyPattern.class);
        assertNotNull(result);
        assertEquals(Integer.toString(lineNumber), result.getLineNumber());
        assertEquals(lastModified, result.getExistsFromDate());
        assertEquals(Integer.toString(pointNumber), result.getStopPointNumber());
    }


}
