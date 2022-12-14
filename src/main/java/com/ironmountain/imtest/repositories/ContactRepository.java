package com.ironmountain.imtest.repositories;

import com.ironmountain.imtest.model.entities.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Integer> {

    Optional<Contact> findContactByUniquePopulationRegistryCodeIgnoreCase(String uniquePopulationRegistryCode);
    Optional<Contact> findContactByUniquePopulationRegistryCodeIgnoreCaseAndIdIsNot(String uniquePopulationRegistryCode, int id);
    @Query("SELECT c FROM Contact c " +
            "WHERE UPPER(c.fullName) like UPPER(CONCAT('%', :filter, '%')) " +
            "OR UPPER(c.address) like UPPER(CONCAT('%', :filter, '%')) " +
            "OR UPPER(c.phone) like UPPER(CONCAT('%', :filter, '%')) " +
            "OR UPPER(c.uniquePopulationRegistryCode) LIKE UPPER(CONCAT('%', :filter, '%')) ")
    Page<Contact> findPagedContacts(@Param("filter") String filter, Pageable pageable);
}
