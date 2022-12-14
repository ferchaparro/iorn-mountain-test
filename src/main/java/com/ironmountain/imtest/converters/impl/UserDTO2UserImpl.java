package com.ironmountain.imtest.converters.impl;

import com.ironmountain.imtest.converters.UserDTO2User;
import com.ironmountain.imtest.model.entities.User;
import com.ironmountain.imtest.transfer.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class UserDTO2UserImpl implements UserDTO2User {
    @Override
    public User convert(UserDTO user) {
        return User.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .role(user.getRole())
                .build();
    }
}
