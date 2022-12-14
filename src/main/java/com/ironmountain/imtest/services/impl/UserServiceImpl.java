package com.ironmountain.imtest.services.impl;

import com.ironmountain.imtest.converters.User2UserDTO;
import com.ironmountain.imtest.converters.UserDTO2User;
import com.ironmountain.imtest.exceptions.BusinessException;
import com.ironmountain.imtest.exceptions.messages.Msg;
import com.ironmountain.imtest.model.entities.User;
import com.ironmountain.imtest.repositories.UserRepository;
import com.ironmountain.imtest.security.token.auth.TokenProvider;
import com.ironmountain.imtest.services.BaseService;
import com.ironmountain.imtest.services.UserService;
import com.ironmountain.imtest.transfer.UserDTO;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UserServiceImpl extends BaseService<UserDTO> implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDTO2User dto2User;
    private final User2UserDTO user2Dto;
    private final TokenProvider jwtTokenProvider;

    private final static String ROLE_PREFIX = "ROLE_";

    @Override
    public boolean assertCondition(UserDTO user) {
        return existUser(user.getId(), user.getUsername());
    }

    private boolean existUser(Integer id, String username) {
        return (Objects.isNull(id)?
                userRepository.findUserByUsernameIgnoreCase(username):
                userRepository.findUserByUsernameIgnoreCaseAndIdIsNot(username, id))
                .isPresent();
    }

    @Override
    @Transactional
    public UserDTO createUser(UserDTO user) {
        User _user = dto2User.convert(assertNotExist(assertNotNull(user, Msg.USER_CANT_BE_NULL), Msg.USER_EXISTS));

        if(Objects.isNull(_user)) {
            throw new BusinessException(Msg.USER_CANT_BE_NULL);
        }
        String password = user.getPassword();

        _user.setPassword(passwordEncoder.encode(password));
        return user2Dto.convert(userRepository.save(_user));
    }

    @Override
    @Transactional(readOnly = true)
    public String authenticate(String username, String password, boolean keepLogged) {
        if (Objects.isNull(username)) {
            throw new BusinessException(Msg.USER_CANT_BE_NULL);
        }

        User user = userRepository.findUserByUsernameIgnoreCase(username)
                .orElseThrow(()->new BusinessException(Msg.USER_NOT_FOUND));

        if (Objects.isNull(password) || !passwordEncoder.matches(password, user.getPassword())) {
            throw new BusinessException(Msg.INCORRECT_PASSWORD);
        }
        return authenticate(user, keepLogged);
    }

    private String authenticate(User user, boolean keepLogged) {

        List<String> roles = List.of(ROLE_PREFIX + user.getRole());

        return jwtTokenProvider.createToken(user.getId(),
                user.getUsername(),
                keepLogged,
                roles);
    }

}
