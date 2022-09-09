package ru.piskunov.web.security;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static ru.piskunov.web.security.UserRole.USER;

@ActiveProfiles("test")
@SpringBootTest
@RunWith(SpringRunner.class)
public class CustomUserDetailsServiceTest {

    @Autowired
    CustomUserDetailsService subj;

    @Test
    public void loadUserByUsername() {
        UserDetails userDetails = subj.loadUserByUsername("alex@yandex.ru");

        assertNotNull("alex@yandex.ru", userDetails.getUsername());
        assertEquals("$2a$10$c/LVrjM0m7tJzuzYppM8u.rKAwQKN6C0e5oPEIyZ5znepRHuacQRW", userDetails.getPassword());
        assertEquals(1, userDetails.getAuthorities().size());
        assertEquals(new CustomGrantedAuthority(USER), userDetails.getAuthorities().iterator().next());
    }
}