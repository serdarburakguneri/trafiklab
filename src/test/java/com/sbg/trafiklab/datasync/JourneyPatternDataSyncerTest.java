package com.sbg.trafiklab.datasync;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sbg.trafiklab.integration.dto.SLJourneyPattern;
import com.sbg.trafiklab.integration.client.SLApiClient;
import com.sbg.trafiklab.service.JourneyPatternService;
import java.io.IOException;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.core.io.ResourceLoader;

class JourneyPatternDataSyncerTest {

    private JourneyPatternDataSyncer journeyPatternDataSyncer;

    @Mock
    private ObjectMapper mockObjectMapper;
    @Mock
    private ResourceLoader mockResourceLoader;
    @Mock
    private JourneyPatternService mockJourneyPatternService;
    @Mock
    private SLApiClient mockSlApiClient;

    @BeforeEach
    void setUp() {
        mockObjectMapper = mock(ObjectMapper.class);
        mockResourceLoader = mock(ResourceLoader.class);
        mockJourneyPatternService = mock(JourneyPatternService.class);
        mockSlApiClient = mock(SLApiClient.class);
        journeyPatternDataSyncer = new JourneyPatternDataSyncer(mockObjectMapper, mockResourceLoader,
                mockJourneyPatternService, mockSlApiClient);
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
