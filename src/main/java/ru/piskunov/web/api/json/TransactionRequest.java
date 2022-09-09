package ru.piskunov.web.api.json;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class TransactionRequest {

    @NotNull
    private Long amount;
    private Long fromAccountId;
    private Long toAccountId;

    @NotNull
    private String categoryTransactionId;
}
