package com.ironmountain.imtest.converters.impl;

import com.ironmountain.imtest.converters.CSVRecord2ContactDTO;
import com.ironmountain.imtest.exceptions.BusinessException;
import com.ironmountain.imtest.exceptions.messages.Msg;
import com.ironmountain.imtest.transfer.ContactDTO;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

@Component
public class CSVRecord2ContactDTOImpl implements CSVRecord2ContactDTO {

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String ADDRESS = "address";
    private static final String PHONE = "phone";
    private static final String CURP = "curp";

    @Override
    public ContactDTO convert(CSVRecord record) {
        try {
            return ContactDTO.builder()
//                .id(Integer.parseInt(record.get(ID)))
                    .fullName(record.get(NAME))
                    .address(record.get(ADDRESS))
                    .phone(record.get(PHONE))
                    .uniquePopulationRegistryCode(record.get(CURP))
                    .build();
        }catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(Msg.FILE_CONTENT_NOT_CORRECT);
        }
    }
}
