package com.sbg.trafiklab.datasyncer;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sbg.trafiklab.integration.dto.SLStopPoint;
import com.sbg.trafiklab.service.StopService;
import java.io.IOException;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ResourceLoader;

public class StopDataSyncerTest {

    @Mock
    private ObjectMapper mockObjectMapper;

    @Mock
    private ResourceLoader mockResourceLoader;

    @Mock
    private StopService mockStopService;

    @InjectMocks
    private StopDataSyncer stopDataSyncer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testConvertJsonNode() throws IOException {
        var mockJsonParser = mock(JsonParser.class);
        var pointNumber = 1;
        var pointName = "Fridhemsplan";
        var lastModified = new Date();
        var sampleData = new SLStopPoint(pointNumber, pointName, 1, 1, 1, "", "", lastModified, lastModified);

        when(mockObjectMapper.readValue(mockJsonParser, SLStopPoint.class)).thenReturn(sampleData);

        var result = stopDataSyncer.convertJsonNodeToEntity(mockJsonParser);

        verify(mockObjectMapper, times(1)).readValue(mockJsonParser, SLStopPoint.class);
        assertNotNull(result);
        assertEquals(Integer.toString(pointNumber), result.getStopNumber());
        assertEquals(pointName, result.getStopName());
        assertEquals(lastModified, result.getExistsFromDate());
    }

}

