package com.sbg.trafiklab.datasyncer;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sbg.trafiklab.integration.dto.SLLineData;
import com.sbg.trafiklab.integration.client.SLApiClient;
import com.sbg.trafiklab.service.LineService;
import java.io.IOException;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.core.io.ResourceLoader;

class LineDataSyncerTest {

    private LineDataSyncer lineDataSyncer;
    private ObjectMapper mockObjectMapper;
    private ResourceLoader mockResourceLoader;
    private LineService mockLineService;

    @BeforeEach
    void setUp() {
        mockObjectMapper = mock(ObjectMapper.class);
        mockResourceLoader = mock(ResourceLoader.class);
        mockLineService = mock(LineService.class);
        lineDataSyncer = new LineDataSyncer(mockObjectMapper, mockResourceLoader, mockLineService);
    }

    @Test
    void testConvertJsonNode() throws IOException {
        var mockJsonParser = mock(JsonParser.class);
        var lineNumber = 1;
        var lastModified = LocalDate.now().minusDays(1);
        var sampleData = new SLLineData(lineNumber, "1", "", "BUS", lastModified, lastModified);
        when(mockObjectMapper.readValue(mockJsonParser, SLLineData.class)).thenReturn(sampleData);

        var result = lineDataSyncer.convertJsonNodeToEntity(mockJsonParser);

        verify(mockObjectMapper, times(1)).readValue(mockJsonParser, SLLineData.class);
        assertNotNull(result);
        assertEquals(Integer.toString(lineNumber), result.getLineNumber());
        assertEquals(lastModified, result.getExistsFromDate());
    }


}
