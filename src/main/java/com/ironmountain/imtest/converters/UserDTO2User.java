package com.ironmountain.imtest.converters;

import com.ironmountain.imtest.model.entities.User;
import com.ironmountain.imtest.transfer.UserDTO;
import org.springframework.core.convert.converter.Converter;

public interface UserDTO2User extends Converter<UserDTO, User> {
}
