package com.ironmountain.imtest.transfer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class UserDTO {
    private Integer id;
    private String username;
    private String password;
    private String role;
}
