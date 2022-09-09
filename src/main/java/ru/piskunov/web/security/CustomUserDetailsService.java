package ru.piskunov.web.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.piskunov.web.entity.User;
import ru.piskunov.web.repository.UserRepository;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);
        if (!user.isPresent()) {
            throw new UsernameNotFoundException("Can't find user with email" + email);
        }
        return new CustomUserDetails(
                user.get().getId(),
                user.get().getEmail(),
                user.get().getPassword(),
                user.get().getRoles().stream()
                        .map(CustomGrantedAuthority::new)
                        .collect(Collectors.toList()));
    }
}
