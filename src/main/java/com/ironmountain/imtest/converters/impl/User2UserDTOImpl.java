package com.ironmountain.imtest.converters.impl;

import com.ironmountain.imtest.converters.User2UserDTO;
import com.ironmountain.imtest.model.entities.User;
import com.ironmountain.imtest.transfer.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class User2UserDTOImpl implements User2UserDTO {
    @Override
    public UserDTO convert(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .role(user.getRole())
                .build();
    }
}
