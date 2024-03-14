package com.sbg.trafiklab.datasync;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sbg.trafiklab.integration.dto.SLStopPoint;
import com.sbg.trafiklab.integration.client.SLApiClient;
import com.sbg.trafiklab.service.StopPointService;
import java.io.IOException;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.core.io.ResourceLoader;

class StopPointDataSyncerTest {

    private StopPointDataSyncer stopPointDataSyncer;

    @Mock
    private ObjectMapper mockObjectMapper;
    @Mock
    private ResourceLoader mockResourceLoader;
    @Mock
    private StopPointService mockStopPointService;
    @Mock
    private SLApiClient mockSlApiClient;

    @BeforeEach
    void setUp() {
        mockObjectMapper = mock(ObjectMapper.class);
        mockResourceLoader = mock(ResourceLoader.class);
        mockStopPointService = mock(StopPointService.class);
        mockSlApiClient = mock(SLApiClient.class);
        stopPointDataSyncer = new StopPointDataSyncer(mockObjectMapper, mockResourceLoader, mockStopPointService,
                mockSlApiClient);
    }

    @Test
    void testConvertJsonNode() throws IOException {
        var mockJsonParser = mock(JsonParser.class);
        var pointNumber = 1;
        var pointName = "Fridhemsplan";
        var lastModified = LocalDate.now().minusDays(1);
        var sampleData = new SLStopPoint(pointNumber, pointName, 1, 1, 1, "", "", lastModified, lastModified);

        when(mockObjectMapper.readValue(mockJsonParser, SLStopPoint.class)).thenReturn(sampleData);

        var result = stopPointDataSyncer.convertJsonNodeToEntity(mockJsonParser);

        verify(mockObjectMapper, times(1)).readValue(mockJsonParser, SLStopPoint.class);
        assertNotNull(result);
        assertEquals(Integer.toString(pointNumber), result.getStopPointNumber());
        assertEquals(pointName, result.getStopPointName());
        assertEquals(lastModified, result.getExistsFromDate());
    }

}

