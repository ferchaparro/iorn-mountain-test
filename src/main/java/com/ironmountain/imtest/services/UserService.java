package com.ironmountain.imtest.services;

import com.ironmountain.imtest.transfer.UserDTO;
import org.springframework.transaction.annotation.Transactional;

public interface UserService {
    @Transactional
    UserDTO createUser(UserDTO user);

    @Transactional(readOnly = true)
    String authenticate(String username, String password, boolean keepLogged);
}
