package ru.piskunov.web.service.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Accessors(chain = true)
public class TransactionDTO {
    private Long id;
    private Long amount;
    private AccountDTO fromAccount;
    private AccountDTO toAccount;
    private LocalDateTime dateAndTime;
    private List<CategoryTransactionDTO> categoryTransactionDTOS;
}
