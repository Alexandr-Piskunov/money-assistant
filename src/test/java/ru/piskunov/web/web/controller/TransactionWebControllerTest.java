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
import ru.piskunov.web.service.CategoryTransactionService;
import ru.piskunov.web.service.TransactionService;
import ru.piskunov.web.service.dto.AccountDTO;
import ru.piskunov.web.service.dto.TransactionDTO;
import ru.piskunov.web.service.dto.UserDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionWebController.class)
@Import({SecurityConfiguration.class, MockSecurityConfiguration.class})
@RunWith(SpringRunner.class)
public class TransactionWebControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    TransactionService transactionService;

    @MockBean
    AccountService accountService;

    @MockBean
    CategoryTransactionService categoryTransactionService;

    @WithUserDetails(value = "alex@yandex.ru", userDetailsServiceBeanName = "userDetailsService")
    @Test
    public void getTransactionCreate_transaction_Found() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        UserDTO userDTO = new UserDTO()
                .setUserName("alex")
                .setEmail("alex@yandex.ru");
        when(transactionService.create(1L, 1000L, 1L, 2L, asList(1L, 2L))).thenReturn(Optional.of(new TransactionDTO()
                .setId(1L)
                .setAmount(1000L)
                .setToAccount(new AccountDTO()
                        .setId(2L)
                        .setBalance(3000L)
                        .setAccountName("from")
                        .setUserDTO(userDTO))
                .setFromAccount(new AccountDTO()
                        .setId(1L)
                        .setBalance(3000L)
                        .setAccountName("from")
                        .setUserDTO(userDTO))
                .setDateAndTime(now)));
        mockMvc.perform(get("/transaction/create").param("categoryId", "1,2")
                        .param("amount", "1000")
                        .param("fromAccountId", "1")
                        .param("toAccountId", "2"))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/account"));
    }

    @WithUserDetails(value = "alex@yandex.ru", userDetailsServiceBeanName = "userDetailsService")
    @Test
    public void getTransaction_transaction_Not_Create() throws Exception {
        List<Long> list = asList(1L, 2L);
        when(transactionService.create(1L, 1000L, 1L, 2L, list)).thenReturn(Optional.empty());
        mockMvc.perform(get("/transaction/create").param("categoryId", "1,2")
                        .param("amount", "1000")
                        .param("fromAccountId", "1")
                        .param("toAccountId", "2"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("error", "Произошла ошибка"))
                .andExpect(view().name("transactionCreate"));
    }

    @WithUserDetails(value = "alex@yandex.ru", userDetailsServiceBeanName = "userDetailsService")
    @Test
    public void getTransaction_transaction_BadAmount() throws Exception {
        mockMvc.perform(get("/transaction/create").param("categoryId", "1,2")
                        .param("amount", "-1000")
                        .param("fromAccountId", "1")
                        .param("toAccountId", "2"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("badAmount", "Неверная сумма"))
                .andExpect(view().name("transactionCreate"));
    }

    @WithUserDetails(value = "alex@yandex.ru", userDetailsServiceBeanName = "userDetailsService")
    @Test
    public void getTransaction_transaction_NoAmount() throws Exception {
        mockMvc.perform(get("/transaction/create").param("categoryId", "1,2")
                        .param("amount", "")
                        .param("fromAccountId", "1")
                        .param("toAccountId", "2"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("check", "Напоминание"))
                .andExpect(view().name("transactionCreate"));
    }
}