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
import ru.piskunov.web.service.AccountService;
import ru.piskunov.web.service.dto.AccountDTO;
import ru.piskunov.web.service.dto.UserDTO;

import java.util.Optional;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
@Import({SecurityConfiguration.class, MockSecurityConfiguration.class})
@RunWith(SpringRunner.class)
public class AccountControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    AccountService accountService;

    @Before
    public void setUp() throws Exception {
        when(accountService.findAll(1L)).thenReturn(asList(new AccountDTO()
                .setId(1L)
                .setBalance(321L)
                .setAccountName("test1")
                .setUserDTO(new UserDTO()
                        .setUserName("alex")
                        .setEmail("alex@yandex.ru")
                        .setId(1L))));
    }

    @WithUserDetails(value = "alex@yandex.ru", userDetailsServiceBeanName = "userDetailsService")
    @Test
    public void getAllAccountByUserId() throws Exception {
        mockMvc.perform(get("/api/account"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\n" +
                        "  \"headers\": {},\n" +
                        "  \"body\": {\n" +
                        "    \"id\": 1,\n" +
                        "    \"accountName\": \"test1\",\n" +
                        "    \"balance\": 321,\n" +
                        "    \"userDTO\": {\n" +
                        "      \"id\": 1,\n" +
                        "      \"email\": \"alex@yandex.ru\",\n" +
                        "      \"userName\": \"alex\"\n" +
                        "    }\n" +
                        "  }\n" +
                        "}]"));
    }

    @WithUserDetails(value = "alex@yandex.ru", userDetailsServiceBeanName = "userDetailsService")
    @Test
    public void create_accontFound() throws Exception {
        when(accountService.create(1L, "newname")).thenReturn(Optional.of(new AccountDTO()
                .setId(1L)
                .setBalance(0L)
                .setAccountName("newname")
                .setUserDTO(new UserDTO()
                        .setId(1L)
                        .setUserName("alex")
                        .setEmail("alex@yandex.ru"))));
        mockMvc.perform(post("/api/account/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"newname\"}")
                ).andExpect(status().isOk())
                .andExpect(content().json("{\n" +
                        "  \"id\": 1,\n" +
                        "  \"accountName\": \"newname\",\n" +
                        "  \"balance\": 0,\n" +
                        "  \"userDTO\": {\n" +
                        "    \"id\": 1,\n" +
                        "    \"email\": \"alex@yandex.ru\",\n" +
                        "    \"userName\": \"alex\"\n" +
                        "  }\n" +
                        "}"));
    }

    @WithUserDetails(value = "alex@yandex.ru", userDetailsServiceBeanName = "userDetailsService")
    @Test
    public void create_accountNotFound() throws Exception {
        when(accountService.create(1L, "newname")).thenReturn(Optional.empty());
        mockMvc.perform(post("/api/account/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"newname\"}"))
                .andExpect(status().is(400));
    }

    @WithUserDetails(value = "alex@yandex.ru", userDetailsServiceBeanName = "userDetailsService")
    @Test
    public void rename_accountFound() throws Exception {
        when(accountService.rename("newname", 1L, 1L)).thenReturn(Optional.of(new AccountDTO()
                .setId(1L)
                .setBalance(333L)
                .setAccountName("newname")
                .setUserDTO(new UserDTO()
                        .setUserName("alex")
                        .setEmail("alex@yandex.ru")
                        .setId(1L))));
        mockMvc.perform(post("/api/account/rename")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"id\": 1,\n" +
                                "  \"name\": \"newname\"" +
                                "}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\n" +
                        "  \"id\": 1,\n" +
                        "  \"accountName\": \"newname\",\n" +
                        "  \"balance\": 333,\n" +
                        "  \"userDTO\": {\n" +
                        "    \"id\": 1,\n" +
                        "    \"email\": \"alex@yandex.ru\",\n" +
                        "    \"userName\": \"alex\"\n" +
                        "  }\n" +
                        "}"));
    }

    @WithUserDetails(value = "alex@yandex.ru", userDetailsServiceBeanName = "userDetailsService")
    @Test
    public void rename_accountNotFound() throws Exception {
        when(accountService.rename("newname", 1L, 1L)).thenReturn(Optional.empty());
        mockMvc.perform(post("/api/account/rename")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"id\": 1,\n" +
                                "  \"name\": \"newname\"" +
                                "}"))
                .andExpect(status().is(400));
    }

    @WithUserDetails(value = "alex@yandex.ru", userDetailsServiceBeanName = "userDetailsService")
    @Test
    public void delete_isTrue() throws Exception {
        when(accountService.delete(1L, 1L)).thenReturn(true);
        mockMvc.perform(post("/api/account/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"id\": 1\n" +
                                "}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\n" +
                        "  \"result\": true\n" +
                        "}"));
    }

    @WithUserDetails(value = "alex@yandex.ru", userDetailsServiceBeanName = "userDetailsService")
    @Test
    public void delete_isFalse() throws Exception {
        when(accountService.delete(1L, 1L)).thenReturn(false);
        mockMvc.perform(post("/api/account/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"id\": 1\n" +
                                "}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\n" +
                        "  \"result\": false\n" +
                        "}"));
    }
}