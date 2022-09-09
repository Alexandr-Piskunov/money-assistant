package ru.piskunov.web.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.piskunov.web.converter.UserModelToUserDTOConverter;
import ru.piskunov.web.entity.User;
import ru.piskunov.web.repository.UserRepository;
import ru.piskunov.web.security.UserRole;
import ru.piskunov.web.service.dto.UserDTO;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserModelToUserDTOConverter userDTOConverter;

    @Transactional
    public Optional<UserDTO> auth(String email, String password) {
        Optional<User> byEmail = userRepository.findByEmail(email);
        if (byEmail.isPresent() && passwordEncoder.matches(password, byEmail.get().getPassword())) {
           return byEmail.map(userDTOConverter::convert) ;
        }
        return Optional.empty();
    }


    @Transactional
    public Optional<UserDTO> registration(String email, String password, String userName) {
        if (!userRepository.findByEmail(email).isPresent()) {
            return Optional.of(userRepository.save(
                            new User()
                                    .setEmail(email)
                                    .setUserName(userName)
                                    .setPassword(passwordEncoder.encode(password)))
                                    .setRoles(Collections.singleton(UserRole.USER)))
                    .map(userDTOConverter::convert);
        }
        return Optional.empty();
    }

    @Transactional
    public Optional<UserDTO> findById(Long id) {
        return userRepository.findById(id).map(userDTOConverter::convert);
    }
}
