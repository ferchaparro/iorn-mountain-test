package com.ironmountain.imtest.services.csvprocessor.impl;

import com.ironmountain.imtest.configurations.CSVParserConfiguration;
import com.ironmountain.imtest.converters.CSVRecord2ContactDTO;
import com.ironmountain.imtest.services.csvprocessor.CSVProcessor;

import com.ironmountain.imtest.transfer.ContactDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Stream;

@Component
@AllArgsConstructor
public class CommonsCSVProcessor implements CSVProcessor {
    private final CSVRecord2ContactDTO record2Contact;
    private final CSVParserConfiguration.CSVParserGetter parserGetter;

    @Override
    public Stream<ContactDTO> getCsvData(InputStream input, boolean skipHeaderRecord) {
        try {
            byte[] bytes = input.readAllBytes();
            input.close();
            return parserGetter.getParser(bytes, skipHeaderRecord)
                    .getRecords().stream()
                    .map(record2Contact::convert);
        }catch (IOException e) {
            return Stream.empty();
        }
    }
}
