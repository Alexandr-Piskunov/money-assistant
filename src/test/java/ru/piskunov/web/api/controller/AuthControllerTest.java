package ru.piskunov.web.api.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.piskunov.web.MockSecurityConfiguration;
import ru.piskunov.web.config.SecurityConfiguration;
import ru.piskunov.web.service.AuthService;
import ru.piskunov.web.service.dto.UserDTO;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import({SecurityConfiguration.class, MockSecurityConfiguration.class})
@RunWith(SpringRunner.class)
public class AuthControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    AuthService authService;

    @Before
    public void setUp() throws Exception {
     when(authService.findById(1L)).thenReturn(Optional.of(new UserDTO()
                .setId(1L)
                .setEmail("alex@yandex.ru")
                .setUserName("test")));
    }

    @WithUserDetails(value = "alex@yandex.ru", userDetailsServiceBeanName = "userDetailsService")
    @Test
    public void getPersonalArea() throws Exception {
        mockMvc.perform(get("/api/personal-area"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\n" +
                        "  \"id\": 1,\n" +
                        "  \"email\": \"alex@yandex.ru\",\n" +
                        "  \"userName\": \"test\"\n" +
                        "}"));
    }

    @Test
    public void registration_userFound() throws Exception {
       when(authService.registration("alex@yandex.ru", "password", "test")).thenReturn(Optional.of(new UserDTO()
                .setId(1L)
                .setEmail("alex@yandex.ru")
                .setUserName("test")));
        mockMvc.perform(post("/api/registration").contentType(MediaType.APPLICATION_JSON)
                .content("{\n"+
                        "  \"email\": \"alex@yandex.ru\",\n" +
                        " \"password\": \"password\" ,\n" +
                        "  \"userName\": \"test\"\n" +
                        "}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\n" +
                        "  \"id\": 1,\n" +
                        "  \"email\": \"alex@yandex.ru\",\n" +
                        "  \"userName\": \"test\"\n" +
                        "}"));
    }

    @Test
    public void registration_userNotFound() throws Exception {
        when(authService.registration("alex@yandex.ru", "password", "test")).thenReturn(Optional.empty());
        mockMvc.perform(post("/api/registration").contentType(MediaType.APPLICATION_JSON)
                .content("{\n"+
                        "  \"email\": \"alex@yandex.ru\",\n" +
                        " \"password\": \"password\" ,\n" +
                        "  \"userName\": \"test\"\n" +
                        "}"))
                .andExpect(status().is(400));
    }
}