package com.sbg.trafiklab.datasyncer;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sbg.trafiklab.integration.dto.SLJourneyPattern;
import com.sbg.trafiklab.service.LineService;
import com.sbg.trafiklab.service.StopService;
import java.io.IOException;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ResourceLoader;

public class JourneyPatternDataSyncerTest {

    @Mock
    private ObjectMapper mockObjectMapper;
    @Mock
    private ResourceLoader mockResourceLoader;
    @Mock
    private LineService mockLineService;
    @Mock
    private StopService mockStopService;
    @InjectMocks
    private JourneyPatternDataSyncer journeyPatternDataSyncer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testConvertJsonNode() throws IOException {
        var mockJsonParser = mock(JsonParser.class);
        var lineNumber = 1;
        var pointNumber = 1;
        var lastModified = new Date();
        var sampleData = new SLJourneyPattern(lineNumber, 1, pointNumber, lastModified, lastModified);
        when(mockObjectMapper.readValue(mockJsonParser, SLJourneyPattern.class)).thenReturn(sampleData);

        var result = journeyPatternDataSyncer.convertJsonNodeToEntity(mockJsonParser);

        verify(mockObjectMapper, times(1)).readValue(mockJsonParser, SLJourneyPattern.class);
        assertNotNull(result);
        assertEquals(Integer.toString(lineNumber), result.getLineNumber());
        assertEquals(lastModified, result.getExistsFromDate());
        assertEquals(Integer.toString(pointNumber), result.getStopNumber());
    }

}
