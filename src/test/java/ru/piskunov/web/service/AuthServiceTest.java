package ru.piskunov.web.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.piskunov.web.converter.UserModelToUserDTOConverter;
import ru.piskunov.web.entity.User;
import ru.piskunov.web.repository.UserRepository;
import ru.piskunov.web.service.dto.UserDTO;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AuthServiceTest {

    @InjectMocks
    AuthService subj;

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    UserModelToUserDTOConverter userDTOConverter;

    @Test
    public void auth_userNotFound() {
        when(userRepository.findByEmail("alex@yandex.ru")).thenReturn(Optional.empty());

        assertFalse(subj.auth("alex@yandex.ru", "password").isPresent());

        verify(userRepository, times(1)).findByEmail("alex@yandex.ru");
        verifyNoInteractions(userDTOConverter);
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    public void auth_userFound() {
        User userModel = new User()
                .setId(1L)
                .setUserName("alex@yandex.ru")
                .setPassword("hex")
                .setUserName("userTEST");
        when(userRepository.findByEmail("alex@yandex.ru")).thenReturn(Optional.of(userModel));
        when(passwordEncoder.matches("password", userModel.getPassword())).thenReturn(true);
        UserDTO userDTO = new UserDTO();
        when(userDTOConverter.convert(userModel)).thenReturn(userDTO);
        Optional<UserDTO> user = subj.auth("alex@yandex.ru", "password");

        assertTrue(user.isPresent());
        assertEquals(userDTO, user.get());

        verify(passwordEncoder, times(1)).matches("password", userModel.getPassword());
        verify(userRepository, times(1)).findByEmail("alex@yandex.ru");
        verify(userDTOConverter, times(1)).convert(userModel);
    }

    @Test
    public void registration_userNotCreate() {
        User user = new User()
                .setId(1L)
                .setUserName("newuser@mail.ru")
                .setPassword("hex")
                .setUserName("newuser");
        when(userRepository.findByEmail("newuser@mail.ru")).thenReturn(Optional.of(user));

        subj.registration("newuser@mail.ru", "password", "newuser");

        verifyNoInteractions(passwordEncoder);
        verify(userRepository, times(1)).findByEmail("newuser@mail.ru");
        verifyNoInteractions(userDTOConverter);
    }

    @Test
    public void registration_userCreate() {
        when(passwordEncoder.encode("password")).thenReturn("hex");
        when(userRepository.findByEmail("newuser@mail.ru")).thenReturn(Optional.empty());
        User userModel = new User()
                .setUserName("newuser@mail.ru")
                .setPassword("hex")
                .setUserName("newuser");
        when(userRepository.save(new User()
                .setEmail("newuser@mail.ru")
                .setPassword("hex")
                .setUserName("newuser"))).thenReturn(userModel);
        UserDTO userDTO = new UserDTO();
        when(userDTOConverter.convert(userModel)).thenReturn(userDTO);
        Optional<UserDTO> user = subj.registration("newuser@mail.ru", "password", "newuser");

        assertTrue(user.isPresent());
        assertEquals(userDTO, user.get());

        verify(passwordEncoder, times(1)).encode("password");
        verify(userRepository, times(1)).findByEmail("newuser@mail.ru");
        verify(userRepository, times(1)).save(new User().setEmail("newuser@mail.ru")
                .setPassword("hex")
                .setUserName("newuser"));
        verify(userDTOConverter, times(1)).convert(userModel);
    }

    @Test
    public void getOne_userFound(){
        User user = new User()
                .setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        UserDTO userDTO = new UserDTO();
        when(userDTOConverter.convert(user)).thenReturn(userDTO);

        Optional<UserDTO> one = subj.findById(1L);

        assertTrue(one.isPresent());
        assertEquals(userDTO, one.get());

        verify(userDTOConverter, times(1)).convert(user);
        verify(userRepository, times(1)).findById(1L);
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    public void getOne_userNotFound(){
        when(userRepository.findById(5L)).thenReturn(Optional.empty());

        assertFalse(subj.findById(5L).isPresent());

        verify(userRepository, times(1)).findById(5L);
        verifyNoInteractions(passwordEncoder);
        verifyNoInteractions(userDTOConverter);
    }
}