package com.ironmountain.imtest.services;

import com.ironmountain.imtest.converters.UserDTO2User;
import com.ironmountain.imtest.exceptions.BusinessException;
import com.ironmountain.imtest.exceptions.messages.Msg;
import com.ironmountain.imtest.model.entities.User;
import com.ironmountain.imtest.repositories.UserRepository;
import com.ironmountain.imtest.security.token.auth.TokenProvider;
import com.ironmountain.imtest.transfer.UserDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTests {
    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserDTO2User dto2User;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private TokenProvider jwtTokenProvider;

    @Autowired
    private UserService userService;

    @Test
    @DisplayName("Debe lanzar BusinessException (USER_CANT_BE_NULL) en createUser si user es null")
    public void shouldThrowBusinessExceptionIfNullCreateUser(){

        Assertions.assertThrows(BusinessException.class, () -> userService.createUser(null), Msg.USER_CANT_BE_NULL.name());
    }

    @Test
    @DisplayName("Debe lanzar BusinessException (USER_EXISTS) en createUser cuando existe el usuario")
    public void shouldThrowBusinessExceptionIfUserExists(){
        UserDTO user = UserDTO.builder().username("admin")
                .password("admin123")
                .role("ADMIN")
                .build();
        when(userRepository.findUserByUsernameIgnoreCase(anyString())).thenReturn(Optional.of(User.builder().build()));
        Assertions.assertThrows(BusinessException.class, () -> userService.createUser(user), Msg.USER_EXISTS.name());
    }

    @Test
    @DisplayName("Debe lanzar BusinessException (USER_EXISTS) en save cuando existe el usuario en una modificacion")
    public void shouldThrowBusinessExceptionIfUserExistsWhenEdit(){
        UserDTO user = UserDTO.builder().username("admin")
                .id(1)
                .password("admin123")
                .role("ADMIN")
                .build();
        when(userRepository.findUserByUsernameIgnoreCaseAndIdIsNot(anyString(), anyInt())).thenReturn(Optional.of(User.builder().build()));
        Assertions.assertThrows(BusinessException.class, () -> userService.createUser(user), Msg.USER_EXISTS.name());
    }

    @Test
    @DisplayName("Debe lanzar BusinessException (USER_CANT_BE_NULL) en createUser cuando convert regresa null")
    public void shouldThrowBusinessExceptionIfConverterReturnNull(){
        UserDTO user = UserDTO.builder().username("admin")
                .password("admin123")
                .role("ADMIN")
                .build();
        when(userRepository.findUserByUsernameIgnoreCase(anyString())).thenReturn(Optional.empty());
        when(dto2User.convert(any(UserDTO.class))).thenReturn(null);
        Assertions.assertThrows(BusinessException.class, () -> userService.createUser(user), Msg.USER_CANT_BE_NULL.name());
    }

    @Test
    @DisplayName("Debe lanzar BusinessException (USER_CANT_BE_NULL) en createUser cuando convert regresa null")
    public void shouldThrowBusinessExceptionIfConverterReturnNullWhenEdit(){
        UserDTO user = UserDTO.builder()
                .id(1)
                .username("admin")
                .password("admin123")
                .role("ADMIN")
                .build();
        when(userRepository.findUserByUsernameIgnoreCaseAndIdIsNot(anyString(), anyInt())).thenReturn(Optional.empty());
        when(dto2User.convert(any(UserDTO.class))).thenReturn(null);
        Assertions.assertThrows(BusinessException.class, () -> userService.createUser(user), Msg.USER_CANT_BE_NULL.name());
    }

    @Test
    @DisplayName("Debe crear un contacto con id 1")
    public void shouldCreateUser(){
        UserDTO user = UserDTO.builder()
                .username("admin")
                .password("admin123")
                .role("ADMIN")
                .build();
        when(userRepository.findUserByUsernameIgnoreCase(anyString())).thenReturn(Optional.empty());
        when(dto2User.convert(any(UserDTO.class))).thenReturn(User.builder()
                .username("admin")
                .password("admin123")
                .role("ADMIN")
                .build());
        when(userRepository.save(any(User.class))).thenReturn(User.builder().id(1).build());

        Assertions.assertEquals(1, userService.createUser(user).getId());
    }

    @Test
    @DisplayName("Debe lanzar BusinessException (USER_NOT_FOUND) en authenticate cuando no encuentra el usuario")
    public void shouldThrowBusinessExceptionOnDeleteIfContactNotFound(){
        when(userRepository.findUserByUsernameIgnoreCase(anyString())).thenReturn(Optional.empty());
        Assertions.assertThrows(BusinessException.class, () -> userService.authenticate("admin", "admin123", false), Msg.USER_NOT_FOUND.name());
    }

    @Test
    @DisplayName("Debe lanzar BusinessException (INCORRECT_PASSWORD) en authenticate cuando contraseña es incorrecta")
    public void shouldThrowBusinessExceptionOnAuthenticateIfPasswordIsNotCorrect(){
        when(userRepository.findUserByUsernameIgnoreCase(anyString())).thenReturn(Optional.of(User.builder().id(1).username("admin").build()));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);
        Assertions.assertThrows(BusinessException.class, () -> userService.authenticate("admin", "admin123", false), Msg.INCORRECT_PASSWORD.name());
    }

    @Test
    @DisplayName("Debe regresar un token")
    public void shouldReturnAuthToken(){
        when(userRepository.findUserByUsernameIgnoreCase(anyString())).thenReturn(Optional.of(User.builder().id(1).username("admin").password("pass").build()));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtTokenProvider.createToken(anyLong(), anyString(), anyBoolean(), anyList())).thenReturn("UN_TOKEN");
        Assertions.assertNotNull(userService.authenticate("admin", "admin123", false));
    }

}
