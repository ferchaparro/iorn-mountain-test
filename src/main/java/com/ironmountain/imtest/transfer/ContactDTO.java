package com.ironmountain.imtest.transfer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@AllArgsConstructor
@Builder
@Data
public class ContactDTO {
    private Integer id;
    private String fullName;
    private String address;
    private String phone;
    private String uniquePopulationRegistryCode;
    private Date creationDate;
    private Date lastUpdate;
}
