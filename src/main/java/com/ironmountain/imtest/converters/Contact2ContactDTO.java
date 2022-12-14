package com.ironmountain.imtest.converters;

import com.ironmountain.imtest.model.entities.Contact;
import com.ironmountain.imtest.transfer.ContactDTO;
import org.springframework.core.convert.converter.Converter;

public interface Contact2ContactDTO extends Converter<Contact, ContactDTO> {
}
