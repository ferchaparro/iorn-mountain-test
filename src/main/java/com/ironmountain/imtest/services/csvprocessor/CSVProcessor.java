package com.ironmountain.imtest.services.csvprocessor;

import com.ironmountain.imtest.transfer.ContactDTO;

import java.io.InputStream;
import java.util.stream.Stream;

public interface CSVProcessor {
    Stream<ContactDTO> getCsvData(InputStream input, boolean skipHeaderRecord);
}
