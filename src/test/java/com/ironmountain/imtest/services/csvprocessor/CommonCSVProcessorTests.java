package com.ironmountain.imtest.services.csvprocessor;

import com.ironmountain.imtest.configurations.CSVParserConfiguration;
import com.ironmountain.imtest.converters.CSVRecord2ContactDTO;
import org.apache.commons.csv.CSVParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@DisplayName("Pruebas CommonCSVProcessor service")
public class CommonCSVProcessorTests {

    @MockBean
    private CSVRecord2ContactDTO record2Contact;
    @MockBean
    private CSVParserConfiguration.CSVParserGetter parserGetter;
    @MockBean
    private CSVParser csvParser;

    @Autowired
    private CSVProcessor csvProcessor;

    @Test
    @DisplayName("Deberia regresar un Stream vacio")
    public void shouldReturnEmptyStream() throws IOException {

        when(csvParser.getRecords()).thenReturn(List.of());
//        when(parserGetter.getParser(any(InputStream.class), anyBoolean())).thenReturn(csvParser);
        Assertions.assertEquals(0, csvProcessor.getCsvData(null, true).count());
    }

}
