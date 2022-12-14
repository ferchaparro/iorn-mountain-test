package com.ironmountain.imtest.services.impl;

import com.ironmountain.imtest.converters.Contact2ContactDTO;
import com.ironmountain.imtest.converters.ContactDTO2Contact;
import com.ironmountain.imtest.exceptions.BusinessException;
import com.ironmountain.imtest.exceptions.messages.Msg;
import com.ironmountain.imtest.model.entities.Contact;
import com.ironmountain.imtest.repositories.ContactRepository;
import com.ironmountain.imtest.services.BaseService;
import com.ironmountain.imtest.services.ContactService;
import com.ironmountain.imtest.services.csvprocessor.CSVProcessor;
import com.ironmountain.imtest.transfer.CSVProcessedResults;
import com.ironmountain.imtest.transfer.ContactDTO;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ContactServiceImpl extends BaseService<ContactDTO> implements ContactService {
    private final ContactRepository contactRepository;
    private final Contact2ContactDTO contact2Dto;
    private final ContactDTO2Contact dto2Contact;
    private final CSVProcessor csvProcessor;

    private static final String MIME_TYPE_CSV = "text/csv";

    @Override
    public boolean assertCondition(ContactDTO contact) {
        return existContact(contact.getId(), contact.getUniquePopulationRegistryCode());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existContact(Integer id, String uniquePopulationRegistryCode) {
        return (Objects.isNull(id)?
                contactRepository.findContactByUniquePopulationRegistryCodeIgnoreCase(uniquePopulationRegistryCode):
                contactRepository.findContactByUniquePopulationRegistryCodeIgnoreCaseAndIdIsNot(uniquePopulationRegistryCode, id))
                .isPresent();
    }

    @Override
    @Transactional(readOnly = true)
    public ContactDTO findById(int id) {
        return contactRepository.findById(id)
                .map(contact2Dto::convert)
                .orElseThrow(()->new BusinessException(Msg.CONTACT_NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContactDTO> findPagedContacts(int pageNumber, int pageSize, String order, String sort, String filter) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(new Sort.Order(Sort.Direction.fromString(order), sort).ignoreCase()));
        Page<Contact> page = contactRepository.findPagedContacts(filter, pageable);
        return new PageImpl<>(page.getContent().stream()
                .map(contact2Dto::convert)
                .collect(Collectors.toList()), page.getPageable(), page.getTotalElements());
    }

    @Override
    @Transactional
    public ContactDTO save(ContactDTO contact) {
        Contact _contact = dto2Contact.convert(assertNotExist(assertNotNull(contact, Msg.CONTACT_CANT_BE_NULL), Msg.CONTACT_EXISTS));

        if(Objects.isNull(_contact)) {
            throw new BusinessException(Msg.CONTACT_CANT_BE_NULL);
        }
        return contact2Dto.convert(contactRepository.save(_contact));
    }

    @Override
    @Transactional
    public void delete(int id) {
        Contact _contact = contactRepository.findById(id)
                .orElseThrow(()-> new BusinessException(Msg.CONTACT_NOT_FOUND));
        contactRepository.delete(_contact);
    }

    @Override
    @Transactional
    public CSVProcessedResults saveAllFromCSV(InputStream input, boolean skipHeaderRecord, String contentType) {
        if(!MIME_TYPE_CSV.equalsIgnoreCase(contentType)){
            throw new BusinessException(Msg.FILE_NOT_SUPPORTED);
        }
        List<Contact> contacts = csvProcessor.getCsvData(input, skipHeaderRecord)
                .map(contact -> {
                    try {
                        Contact _contact = dto2Contact.convert(assertNotExist(assertNotNull(contact, Msg.CONTACT_CANT_BE_NULL), Msg.CONTACT_EXISTS));
                        if(Objects.isNull(_contact)) {
                            throw new BusinessException(Msg.CONTACT_CANT_BE_NULL);
                        }
                        return _contact;
                    } catch (BusinessException ex) {
                        return null;
                    }
                })
                .collect(Collectors.toList());
        long allRecordsCount = contacts.size();
        List<Contact> recordsToSave = contacts.stream().filter(Objects::nonNull)
                .collect(Collectors.toList());
        long recordsToSaveCount = recordsToSave.size();
        Runnable runnable = ()-> contactRepository.saveAll(recordsToSave);
        new Thread(runnable).start();

        return CSVProcessedResults.builder()
                .toSave(recordsToSaveCount)
                .omitted(allRecordsCount-recordsToSaveCount)
                .build();
    }


}
