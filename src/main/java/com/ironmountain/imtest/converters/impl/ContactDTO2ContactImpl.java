package com.ironmountain.imtest.converters.impl;

import com.ironmountain.imtest.converters.ContactDTO2Contact;
import com.ironmountain.imtest.model.entities.Contact;
import com.ironmountain.imtest.transfer.ContactDTO;
import org.springframework.stereotype.Component;

@Component
public class ContactDTO2ContactImpl implements ContactDTO2Contact {
    @Override
    public Contact convert(ContactDTO contact) {
        return Contact.builder()
                .id(contact.getId())
                .fullName(contact.getFullName())
                .address(contact.getAddress())
                .phone(contact.getPhone())
                .uniquePopulationRegistryCode(contact.getUniquePopulationRegistryCode())

                .build();
    }
}
