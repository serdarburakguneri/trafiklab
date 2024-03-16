package com.sbg.trafiklab.datasyncer;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sbg.trafiklab.integration.dto.SLStopPoint;
import com.sbg.trafiklab.service.StopService;
import java.io.IOException;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ResourceLoader;

class StopDataSyncerTest {

    private StopDataSyncer stopDataSyncer;
    private ObjectMapper mockObjectMapper;
    private ResourceLoader mockResourceLoader;
    private StopService mockStopService;

    @BeforeEach
    void setUp() {
        mockObjectMapper = mock(ObjectMapper.class);
        mockResourceLoader = mock(ResourceLoader.class);
        mockStopService = mock(StopService.class);
        stopDataSyncer = new StopDataSyncer(mockObjectMapper, mockResourceLoader, mockStopService);
    }

    @Test
    void testConvertJsonNode() throws IOException {
        var mockJsonParser = mock(JsonParser.class);
        var pointNumber = 1;
        var pointName = "Fridhemsplan";
        var lastModified = LocalDate.now().minusDays(1);
        var sampleData = new SLStopPoint(pointNumber, pointName, 1, 1, 1, "", "", lastModified, lastModified);

        when(mockObjectMapper.readValue(mockJsonParser, SLStopPoint.class)).thenReturn(sampleData);

        var result = stopDataSyncer.convertJsonNodeToEntity(mockJsonParser);

        verify(mockObjectMapper, times(1)).readValue(mockJsonParser, SLStopPoint.class);
        assertNotNull(result);
        assertEquals(Integer.toString(pointNumber), result.getStopPointNumber());
        assertEquals(pointName, result.getStopPointName());
        assertEquals(lastModified, result.getExistsFromDate());
    }

}

