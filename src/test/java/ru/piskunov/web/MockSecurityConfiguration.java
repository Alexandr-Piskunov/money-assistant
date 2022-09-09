package ru.piskunov.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.piskunov.web.security.CustomGrantedAuthority;
import ru.piskunov.web.security.CustomUserDetails;

import static java.util.Collections.singleton;
import static ru.piskunov.web.security.UserRole.USER;

@Configuration
public class MockSecurityConfiguration {
    @Bean
    public UserDetailsService userDetailsService(){
        return username -> new CustomUserDetails(
                1L,
                "alex@yandex.ru",
                "password",
                singleton(new CustomGrantedAuthority(USER))
        );
    }
}
