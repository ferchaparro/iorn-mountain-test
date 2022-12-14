package com.ironmountain.imtest.configurations;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Configuration
public class CSVParserConfiguration {
    @Bean
    public CSVParserGetter CSVParser() {
        return (bytes, skipHeaderRecord) -> {
            BufferedReader fileReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(bytes), StandardCharsets.UTF_8));
            CSVFormat format = CSVFormat.Builder.create()
                    .setDelimiter(',')
                    .setQuote('"')
                    .setIgnoreSurroundingSpaces(false)
                    .setIgnoreEmptyLines(true)
                    .setRecordSeparator("\r\n")
                    .setSkipHeaderRecord(skipHeaderRecord)
                    .setHeader()
                    .setAllowMissingColumnNames(false)
                    .setIgnoreHeaderCase(true)
                    .setTrim(true)
                    .setTrailingDelimiter(false)
                    .setAutoFlush(false)
                    .setAllowDuplicateHeaderNames(true)
                    .build();
            return new CSVParser(fileReader, format);
        };
    }

    public interface CSVParserGetter {
        CSVParser getParser(byte[] bytes, boolean skipHeaderRecord) throws IOException;
    }
}
