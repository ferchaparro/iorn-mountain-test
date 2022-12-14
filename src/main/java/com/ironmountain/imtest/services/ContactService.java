package com.ironmountain.imtest.services;

import com.ironmountain.imtest.transfer.CSVProcessedResults;
import com.ironmountain.imtest.transfer.ContactDTO;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;

public interface ContactService {
    @Transactional(readOnly = true)
    boolean existContact(Integer id, String uniquePopulationRegistryCode);

    @Transactional(readOnly = true)
    ContactDTO findById(int id);

    @Transactional(readOnly = true)
    Page<ContactDTO> findPagedContacts(int pageNumber, int pageSize, String order, String sort, String filter);

    @Transactional
    ContactDTO save(ContactDTO contact);

    @Transactional
    void delete(int id);

    @Transactional
    CSVProcessedResults saveAllFromCSV(InputStream input, boolean skipHeaderRecord, String contentType);
}
