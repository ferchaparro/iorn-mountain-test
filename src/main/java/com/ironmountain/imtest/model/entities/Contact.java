package com.ironmountain.imtest.model.entities;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="contacts")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String fullName;
    private String address;
    private String phone;
    @Column(unique = true)
    private String uniquePopulationRegistryCode;
    @CreatedDate
    @Column(updatable = false)
    private Date creationDate;
    @LastModifiedDate
    private Date lastUpdate;

    @PrePersist
    public void beforeSave() {
        Date now = new Date();
        creationDate = now;
        lastUpdate = now;
    }

    @PreUpdate
    public void beforeUpdate() {
        lastUpdate = new Date();
    }


}
