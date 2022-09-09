package ru.piskunov.web.api.controller;

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
import ru.piskunov.web.service.TransactionService;
import ru.piskunov.web.service.dto.AccountDTO;
import ru.piskunov.web.service.dto.TransactionDTO;
import ru.piskunov.web.service.dto.UserDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
@Import({SecurityConfiguration.class, MockSecurityConfiguration.class})
@RunWith(SpringRunner.class)
public class TransactionControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    TransactionService transactionService;

    @WithUserDetails(value = "alex@yandex.ru", userDetailsServiceBeanName = "userDetailsService")
    @Test
    public void create_transactionFound() throws Exception {
        LocalDateTime now = LocalDateTime.parse("2022-06-06T13:43:46");
        UserDTO userDTO = new UserDTO()
                .setId(1L)
                .setUserName("alex")
                .setEmail("alex@yandex.ru");
        List<Long> list = asList(1L, 2L);
        TransactionDTO transactionDTO = new TransactionDTO()
                .setId(1L)
                .setAmount(1000L)
                .setToAccount(new AccountDTO()
                        .setId(2L)
                        .setBalance(3000L)
                        .setAccountName("to")
                        .setUserDTO(userDTO))
                .setFromAccount(new AccountDTO()
                        .setId(1L)
                        .setBalance(3000L)
                        .setAccountName("from")
                        .setUserDTO(userDTO))
                .setDateAndTime(now);
        when(transactionService.create(1L, 1000L, 1L, 2L, list)).thenReturn(Optional.of(transactionDTO));
        mockMvc.perform(post("/api/transaction/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"amount\": 1000,\n" +
                                "  \"fromAccountId\": 1,\n" +
                                "  \"toAccountId\" : 2,\n" +
                                "  \"categoryTransactionId\" : \"1,2\"\n" +
                                "}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\n" +
                        "\n" +
                        "  \"id\": 1,\n" +
                        "  \"amount\": 1000,\n" +
                        "  \"fromAccount\": {\n" +
                        "    \"id\": 1,\n" +
                        "    \"accountName\": \"from\",\n" +
                        "    \"balance\": 3000,\n" +
                        "    \"userDTO\": {\n" +
                        "      \"id\": 1,\n" +
                        "      \"email\": \"alex@yandex.ru\",\n" +
                        "      \"userName\": \"alex\"\n" +
                        "    }\n" +
                        "  },\n" +
                        "  \"toAccount\": {\n" +
                        "    \"id\": 2,\n" +
                        "    \"accountName\": \"to\",\n" +
                        "    \"balance\": 3000,\n" +
                        "    \"userDTO\": {\n" +
                        "      \"id\": 1,\n" +
                        "      \"email\": \"alex@yandex.ru\",\n" +
                        "      \"userName\": \"alex\"\n" +
                        "    }\n" +
                        "  },\n" +
                        "  \"dateAndTime\": \"2022-06-06T13:43:46\" \n" +
                        "}"));
    }

    @WithUserDetails(value = "alex@yandex.ru", userDetailsServiceBeanName = "userDetailsService")
    @Test
    public void create_transactionNotFound() throws Exception {
        when(transactionService.create(1L, 1000L, 1L, 2L, asList(1L, 2L))).thenReturn(Optional.empty());
        mockMvc.perform(post("/api/transaction/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"amount\": 1000,\n" +
                                "  \"fromAccountId\": 1,\n" +
                                "  \"toAccountId\" : 2,\n" +
                                "  \"categoryTransactionId\" : \"1,2\"\n" +
                                "}"))
                .andExpect(status().is(400));
    }

    @WithUserDetails(value = "alex@yandex.ru", userDetailsServiceBeanName = "userDetailsService")
    @Test
    public void create_amountNotFound() throws Exception {
        mockMvc.perform(post("/api/transaction/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"fromAccountId\": 1,\n" +
                                "  \"toAccountId\" : 2,\n" +
                                "  \"categoryTransactionId\" : \"1,2\"\n" +
                                "}"))
                .andExpect(status().is(400));
    }
}