package ru.piskunov.web.api.json;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.piskunov.web.service.dto.AccountDTO;
import ru.piskunov.web.service.dto.CategoryTransactionDTO;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class TransactionResponse {
    private long id;
    private long amount;
    private AccountDTO fromAccount;
    private AccountDTO toAccount;
    private LocalDateTime dateAndTime;
    private List<CategoryTransactionDTO> categoryTransactionDTOS;
}
