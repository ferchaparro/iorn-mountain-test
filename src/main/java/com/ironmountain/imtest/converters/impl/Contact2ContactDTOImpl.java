package com.ironmountain.imtest.converters.impl;

import com.ironmountain.imtest.converters.Contact2ContactDTO;
import com.ironmountain.imtest.model.entities.Contact;
import com.ironmountain.imtest.transfer.ContactDTO;
import org.springframework.stereotype.Component;

@Component
public class Contact2ContactDTOImpl implements Contact2ContactDTO {
    @Override
    public ContactDTO convert(Contact contact) {
        return ContactDTO.builder()
                .id(contact.getId())
                .fullName(contact.getFullName())
                .address(contact.getAddress())
                .phone(contact.getPhone())
                .uniquePopulationRegistryCode(contact.getUniquePopulationRegistryCode())
                .creationDate(contact.getCreationDate())
                .lastUpdate(contact.getLastUpdate())
                .build();
    }
}
