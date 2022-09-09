package ru.piskunov.web.web.form;

import lombok.Data;

@Data
public class TransactionForm {
    private Long amount;
    private Long fromAccountId;
    private Long toAccountId;
    private String categoryTransactionId;
}
