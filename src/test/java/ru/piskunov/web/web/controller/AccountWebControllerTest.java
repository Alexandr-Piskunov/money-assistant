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
import ru.piskunov.web.service.AccountService;
import ru.piskunov.web.service.dto.AccountDTO;
import ru.piskunov.web.service.dto.UserDTO;
import ru.piskunov.web.web.form.AccountForm;

import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountWebController.class)
@Import({SecurityConfiguration.class, MockSecurityConfiguration.class})
@RunWith(SpringRunner.class)
public class AccountWebControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    AccountService accountService;

    @WithUserDetails(value = "alex@yandex.ru", userDetailsServiceBeanName = "userDetailsService")
    @Test
    public void findAll() throws Exception {
        UserDTO userDTO = new UserDTO()
                .setId(1L)
                .setUserName("alex")
                .setEmail("alex@yandex.ru");
        List<AccountDTO> list = asList(new AccountDTO()
                .setId(1L)
                .setBalance(321L)
                .setAccountName("test1")
                .setUserDTO(userDTO), new AccountDTO()
                .setId(2L)
                .setBalance(112L)
                .setAccountName("test2")
                .setUserDTO(userDTO));
        when(accountService.findAll(1L)).thenReturn(list);
        mockMvc.perform(get("/account"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("account", list))
                .andExpect(view().name("account"));
    }

    @Test
    public void getAccountCreate() throws Exception {
        mockMvc.perform(get("/account/create"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("form", new AccountForm()))
                .andExpect(view().name("accountCreate"));
    }

    @WithUserDetails(value = "alex@yandex.ru", userDetailsServiceBeanName = "userDetailsService")
    @Test
    public void postAccountCreate_accountFound() throws Exception {
        when(accountService.create(1L, "name")).thenReturn(Optional.of(new AccountDTO()
                .setId(1L)
                .setBalance(0L)
                .setAccountName("name")
                .setUserDTO(new UserDTO()
                        .setId(1L)
                        .setUserName("alex")
                        .setEmail("alex@yandex.ru"))));
        mockMvc.perform(post("/account/create").param("name", "name"))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/account"));
    }

    @WithUserDetails(value = "alex@yandex.ru", userDetailsServiceBeanName = "userDetailsService")
    @Test
    public void postAccountCreate_accountNotFound() throws Exception {
        when(accountService.create(1L, "name")).thenReturn(Optional.empty());
        mockMvc.perform(post("/account/create").param("name", "name"))
                .andExpect(status().isOk())
                .andExpect(view().name("accountCreate"));
    }

    @Test
    public void getAccountRename() throws Exception {
        mockMvc.perform(get("/account/rename/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(model().attribute("form", new AccountForm()))
                .andExpect(model().attribute("id", 1L))
                .andExpect(view().name("accountRename"));
    }

    @WithUserDetails(value = "alex@yandex.ru", userDetailsServiceBeanName = "userDetailsService")
    @Test
    public void postAccountRename_accountNameIsOK() throws Exception {
        when(accountService.rename("newname", 1L, 1L)).thenReturn(Optional.of(new AccountDTO()
                .setId(1L)
                .setBalance(123L)
                .setAccountName("newname")
                .setUserDTO(new UserDTO()
                        .setId(1L)
                        .setUserName("alex")
                        .setEmail("alex@yandex.ru"))));
        mockMvc.perform(post("/account/rename/{id}", 1L).param("name", "newname"))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/account"));

    }

    @WithUserDetails(value = "alex@yandex.ru", userDetailsServiceBeanName = "userDetailsService")
    @Test
    public void postAccountRename_accountNameBad() throws Exception {
        when(accountService.rename("newname", 1L, 1L)).thenReturn(Optional.empty());
        mockMvc.perform(post("/account/rename/{id}", 1L).param("name", "newname"))
                .andExpect(status().isOk())
                .andExpect(view().name("accountRename"));

    }

    @WithUserDetails(value = "alex@yandex.ru", userDetailsServiceBeanName = "userDetailsService")
    @Test
    public void postAccountDelete() throws Exception {
        when(accountService.delete(1L, 1L)).thenReturn(true);
        mockMvc.perform(post("/account/delete/{id}", 1L))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/account"));
    }
}