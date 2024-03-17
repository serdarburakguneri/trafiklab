package com.sbg.trafiklab.datasyncer;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sbg.trafiklab.integration.dto.SLLineData;
import com.sbg.trafiklab.service.LineService;
import java.io.IOException;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ResourceLoader;


public class LineDataSyncerTest {

    @Mock
    private ObjectMapper mockObjectMapper;

    @Mock
    private ResourceLoader mockResourceLoader;

    @Mock
    private LineService mockLineService;

    @InjectMocks
    private LineDataSyncer lineDataSyncer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testConvertJsonNode() throws IOException {
        var mockJsonParser = mock(JsonParser.class);
        var lineNumber = 1;
        var lastModified = new Date();
        var sampleData = new SLLineData(lineNumber, "1", "", "BUS", lastModified, lastModified);
        when(mockObjectMapper.readValue(mockJsonParser, SLLineData.class)).thenReturn(sampleData);

        var result = lineDataSyncer.convertJsonNodeToEntity(mockJsonParser);

        verify(mockObjectMapper, times(1)).readValue(mockJsonParser, SLLineData.class);
        assertNotNull(result);
        assertEquals(Integer.toString(lineNumber), result.getLineNumber());
        assertEquals(lastModified, result.getExistsFromDate());
    }


}
