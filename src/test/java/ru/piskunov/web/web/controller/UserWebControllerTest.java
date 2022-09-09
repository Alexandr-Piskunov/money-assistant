package ru.piskunov.web.web.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.piskunov.web.MockSecurityConfiguration;
import ru.piskunov.web.config.SecurityConfiguration;
import ru.piskunov.web.service.AuthService;
import ru.piskunov.web.service.dto.UserDTO;
import ru.piskunov.web.web.form.RegistrationForm;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserWebController.class)
@Import({SecurityConfiguration.class, MockSecurityConfiguration.class})
@RunWith(SpringRunner.class)
public class UserWebControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    AuthService authService;

    @Test
    public void getLogin() throws Exception {
        mockMvc.perform(get("/login-form"))
                .andExpect(status().isOk())
                .andExpect(view().name("login-form"));
    }

    @WithUserDetails(value = "alex@yandex.ru", userDetailsServiceBeanName = "userDetailsService")
    @Test
    public void index() throws Exception {
        when(authService.findById(1L)).thenReturn(Optional.of(new UserDTO()
                .setId(1L)
                .setEmail("alex@yandex.ru")
                .setUserName("alex")));

        mockMvc.perform(get("/personal-area"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("userId", 1L))
                .andExpect(model().attribute("email", "alex@yandex.ru"))
                .andExpect(view().name("personal-area"));
    }

    @Test
    public void getRegistration() throws Exception {
        mockMvc.perform(get("/registration"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("form", new RegistrationForm()))
                .andExpect(view().name("registration"));
    }

    @Test
    public void postRegistration_userFound() throws Exception {
        when(authService.registration("alex@yandex.ru", "password", "alex")).thenReturn(Optional.of(new UserDTO()
                .setId(1L)
                .setEmail("alex@yandex.ru")
                .setUserName("alex")));
        mockMvc.perform(post("/registration").param("email", "alex@yandex.ru").param("password", "password")
                        .param("name", "alex"))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/login-form"));
    }

    @Test
    public void postRegistration_userNotFound() throws Exception {
        when(authService.registration("alex@yandex.ru", "password", "alex")).thenReturn(Optional.empty());
        mockMvc.perform(post("/registration").param("email", "alex@yandex.ru").param("password", "password")
                        .param("name", "alex"))
                .andExpect(status().is(200))
                .andExpect(view().name("registration"));
    }
}