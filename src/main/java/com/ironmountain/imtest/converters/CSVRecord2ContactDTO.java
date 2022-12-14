package com.ironmountain.imtest.converters;

import com.ironmountain.imtest.transfer.ContactDTO;
import org.apache.commons.csv.CSVRecord;
import org.springframework.core.convert.converter.Converter;

public interface CSVRecord2ContactDTO extends Converter<CSVRecord, ContactDTO> {
}
