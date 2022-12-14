package com.ironmountain.imtest.services;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.ironmountain.imtest.converters.ContactDTO2Contact;
import com.ironmountain.imtest.exceptions.BusinessException;
import com.ironmountain.imtest.exceptions.messages.Msg;
import com.ironmountain.imtest.model.entities.Contact;
import com.ironmountain.imtest.repositories.ContactRepository;
import com.ironmountain.imtest.transfer.ContactDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

@SpringBootTest
@DisplayName("Pruebas contactos service")
public class ContactServiceTests {
    @MockBean
    private ContactRepository contactRepository;

    @MockBean
    private ContactDTO2Contact dto2Contact;

    @Autowired
    private ContactService contactService;

    @Test
    @DisplayName("Debe lanzar BusinessException (CONTACT_CANT_BE_NULL) en save si contact es null")
    public void shouldThrowBusinessExceptionIfNullCreateContact(){

        Assertions.assertThrows(BusinessException.class, () -> contactService.save(null), Msg.CONTACT_CANT_BE_NULL.name());
    }

    @Test
    @DisplayName("Debe lanzar BusinessException (CONTACT_EXISTS) en save cuando existe el contacto")
    public void shouldThrowBusinessExceptionIfContactExists(){
        ContactDTO contact = ContactDTO.builder().fullName("Fernando Gastelum")
                .address("Nogales 932")
                .phone("6441430690")
                .uniquePopulationRegistryCode("GACF870604HSRSHR06")
                .build();
        when(contactRepository.findContactByUniquePopulationRegistryCodeIgnoreCase(anyString())).thenReturn(Optional.of(Contact.builder().build()));
        Assertions.assertThrows(BusinessException.class, () -> contactService.save(contact), Msg.CONTACT_EXISTS.name());
    }

    @Test
    @DisplayName("Debe lanzar BusinessException (CONTACT_EXISTS) en save cuando existe el contacto en una modificacion")
    public void shouldThrowBusinessExceptionIfContactExistsWhenEdit(){
        ContactDTO contact = ContactDTO.builder()
                .id(1)
                .fullName("Fernando Gastelum")
                .address("Nogales 932")
                .phone("6441430690")
                .uniquePopulationRegistryCode("GACF870604HSRSHR06")
                .build();
        when(contactRepository.findContactByUniquePopulationRegistryCodeIgnoreCaseAndIdIsNot(anyString(), anyInt())).thenReturn(Optional.of(Contact.builder().build()));
        Assertions.assertThrows(BusinessException.class, () -> contactService.save(contact), Msg.CONTACT_EXISTS.name());
    }

    @Test
    @DisplayName("Debe lanzar BusinessException (CONTACT_CANT_BE_NULL) en save cuando convert regresa null")
    public void shouldThrowBusinessExceptionIfConverterReturnNull(){
        ContactDTO contact = ContactDTO.builder()
                .fullName("Fernando Gastelum")
                .address("Nogales 932")
                .phone("6441430690")
                .uniquePopulationRegistryCode("GACF870604HSRSHR06")
                .build();
        when(contactRepository.findContactByUniquePopulationRegistryCodeIgnoreCase(anyString())).thenReturn(Optional.empty());
        when(dto2Contact.convert(any(ContactDTO.class))).thenReturn(null);
        Assertions.assertThrows(BusinessException.class, () -> contactService.save(contact), Msg.CONTACT_CANT_BE_NULL.name());
    }

    @Test
    @DisplayName("Debe lanzar BusinessException (CONTACT_CANT_BE_NULL) en save cuando convert regresa null")
    public void shouldThrowBusinessExceptionIfConverterReturnNullWhenEdit(){
        ContactDTO contact = ContactDTO.builder()
                .id(1)
                .fullName("Fernando Gastelum")
                .address("Nogales 932")
                .phone("6441430690")
                .uniquePopulationRegistryCode("GACF870604HSRSHR06")
                .build();
        when(contactRepository.findContactByUniquePopulationRegistryCodeIgnoreCaseAndIdIsNot(anyString(), anyInt())).thenReturn(Optional.empty());
        when(dto2Contact.convert(any(ContactDTO.class))).thenReturn(null);
        Assertions.assertThrows(BusinessException.class, () -> contactService.save(contact), Msg.CONTACT_CANT_BE_NULL.name());
    }

    @Test
    @DisplayName("Debe crear un contacto con id 1")
    public void shouldCreateContact(){
        ContactDTO contact = ContactDTO.builder().fullName("Fernando Gastelum")
                .address("Nogales 932")
                .phone("6441430690")
                .uniquePopulationRegistryCode("GACF870604HSRSHR06")
                .build();
        when(contactRepository.findContactByUniquePopulationRegistryCodeIgnoreCase(anyString())).thenReturn(Optional.empty());
        when(dto2Contact.convert(any(ContactDTO.class))).thenReturn(Contact.builder()
                .fullName("Fernando Gastelum")
                .address("Nogales 932")
                .phone("6441430690")
                .uniquePopulationRegistryCode("GACF870604HSRSHR06")
                .build());
        when(contactRepository.save(any(Contact.class))).thenReturn(Contact.builder().id(1).build());

        Assertions.assertEquals(1, contactService.save(contact).getId());
    }

    @Test
    @DisplayName("Debe lanzar BusinessException (CONTACT_NOT_FOUND) en delete cuando no encuentra el contacto")
    public void shouldThrowBusinessExceptionOnDeleteIfContactNotFound(){
        when(contactRepository.findById(anyInt())).thenReturn(Optional.empty());
        Assertions.assertThrows(BusinessException.class, () -> contactService.delete(1), Msg.CONTACT_NOT_FOUND.name());
    }

    @Test
    @DisplayName("Debe eliminar el contacto")
    public void shouldDeleteContact(){
        when(contactRepository.findById(anyInt())).thenReturn(Optional.of(Contact.builder().id(1).build()));
        doNothing().when(contactRepository).delete(any(Contact.class));
        contactService.delete(1);
        Assertions.assertTrue(true);
    }

    @Test
    @DisplayName("Debe regresar TRUE en exists cuando existe el contacto")
    public void shouldReturnTrueIfContactExistsValidation(){
        when(contactRepository.findContactByUniquePopulationRegistryCodeIgnoreCase(anyString())).thenReturn(Optional.of(Contact.builder().build()));
        Assertions.assertTrue(contactService.existContact(null, "GACF8706"));
    }

    @Test
    @DisplayName("Debe regresar true en exist cuando existe el contacto en una modificacion")
    public void shouldReturnTrueIfContactExistsWithIdValidation(){
        when(contactRepository.findContactByUniquePopulationRegistryCodeIgnoreCaseAndIdIsNot(anyString(), anyInt())).thenReturn(Optional.of(Contact.builder().build()));
        Assertions.assertTrue(contactService.existContact(1, "GACF0706"));
    }

    @Test
    @DisplayName("Debe regresar FALSE en exists cuando no existe el contacto")
    public void shouldReturnFalseINContactExistsValidation(){
        when(contactRepository.findContactByUniquePopulationRegistryCodeIgnoreCase(anyString())).thenReturn(Optional.empty());
        Assertions.assertFalse(contactService.existContact(null, "GACF8706"));
    }

    @Test
    @DisplayName("Debe regresar FALSE en exist cuando existe el contacto en una modificacion")
    public void shouldReturnFalseInContactExistsWithIdValidation(){
        when(contactRepository.findContactByUniquePopulationRegistryCodeIgnoreCaseAndIdIsNot(anyString(), anyInt())).thenReturn(Optional.empty());
        Assertions.assertFalse(contactService.existContact(1, "GACF0706"));
    }

    @Test
    @DisplayName("Debe regresar TRUE en assertCondittion cuando existe el contacto")
    public void shouldReturnTrueIfContactExistsAssertCondition(){
        when(contactRepository.findContactByUniquePopulationRegistryCodeIgnoreCase(anyString())).thenReturn(Optional.of(Contact.builder().build()));
        Assertions.assertTrue(contactService.existContact(null, "GACF8706"));
    }

    @Test
    @DisplayName("Debe regresar true en assertCondittion cuando existe el contacto en una modificacion")
    public void shouldReturnTrueIfContactExistsWithIdAssertCondition(){
        when(contactRepository.findContactByUniquePopulationRegistryCodeIgnoreCaseAndIdIsNot(anyString(), anyInt())).thenReturn(Optional.of(Contact.builder().build()));
        Assertions.assertTrue(contactService.existContact(1, "GACF0706"));
    }

    @Test
    @DisplayName("Debe regresar FALSE en assertCondittion cuando no existe el contacto")
    public void shouldReturnFalseINContactExistsAssertCondition(){
        when(contactRepository.findContactByUniquePopulationRegistryCodeIgnoreCase(anyString())).thenReturn(Optional.empty());
        Assertions.assertFalse(contactService.existContact(null, "GACF8706"));
    }

    @Test
    @DisplayName("Debe regresar FALSE en assertCondittion cuando existe el contacto en una modificacion")
    public void shouldReturnFalseInContactExistsWithIdAssertCondition(){
        when(contactRepository.findContactByUniquePopulationRegistryCodeIgnoreCaseAndIdIsNot(anyString(), anyInt())).thenReturn(Optional.empty());
        Assertions.assertFalse(contactService.existContact(1, "GACF0706"));
    }
}
