package ru.piskunov.web.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.piskunov.web.entity.Transaction;
import ru.piskunov.web.service.dto.TransactionDTO;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TransactionModelToTransactionDTOConverter implements Converter<Transaction, TransactionDTO> {
    private final CategoryTransactionModelToCategoryTransactionDTOConverter converterCategory;
    private final AccountModelToAccountDTOConverter converterAccount;

    @Override
    public TransactionDTO convert(Transaction source) {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setId(source.getId());
        transactionDTO.setAmount(source.getAmount());
        transactionDTO.setFromAccount(converterAccount.convert(source.getFromAccount()));
        transactionDTO.setToAccount(converterAccount.convert(source.getToAccount()));
        transactionDTO.setDateAndTime(source.getDateAndTime());
        transactionDTO.setCategoryTransactionDTOS(source.getCategoryTransaction().stream().map(converterCategory::convert).collect(Collectors.toList()));
        return transactionDTO;
    }

}
