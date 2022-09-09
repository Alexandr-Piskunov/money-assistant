package ru.piskunov.web.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.piskunov.web.converter.TransactionModelToTransactionDTOConverter;
import ru.piskunov.web.entity.Account;
import ru.piskunov.web.entity.Transaction;
import ru.piskunov.web.repository.AccountRepository;
import ru.piskunov.web.repository.CategoryTransactionRepository;
import ru.piskunov.web.repository.TransactionRepository;
import ru.piskunov.web.service.dto.TransactionDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final CategoryTransactionRepository categoryTransactionRepository;
    private final TransactionModelToTransactionDTOConverter converter;

    @Transactional
    public Optional<TransactionDTO> create(Long userId, Long amount, Long fromAccountId, Long toAccountId, List<Long> categoryTransactionId) {
        if (fromAccountId == toAccountId) {
            return Optional.empty();
        }
        Optional<Account> fromAccount = accountRepository.findByIdAndUserId(fromAccountId, userId);
        Optional<Account> toAccount = accountRepository.findByIdAndUserId(toAccountId, userId);
        if (!categoryTransactionId.stream().map(l -> categoryTransactionRepository.findByIdAndUserId(l, userId)).allMatch(Optional::isPresent)) {
            return Optional.empty();
        }
        if (fromAccount.isPresent() && (fromAccount.get().getBalance().compareTo(amount) >= 0)) {
            accountRepository.saveAndFlush(fromAccount.get().setBalance(fromAccount.get().getBalance() - amount));
            toAccount.ifPresent(accountModel -> accountRepository.saveAndFlush(accountModel.setBalance(accountModel.getBalance() + amount)));
            return Optional.of(transactionRepository.saveAndFlush(new Transaction()
                            .setAmount(amount)
                            .setDateAndTime(LocalDateTime.now())
                            .setFromAccount(fromAccount.get())
                            .setToAccount(toAccount.orElse(null))
                            .setCategoryTransaction(categoryTransactionId.stream()
                                    .map(categoryTransactionRepository::getById)
                                    .collect(Collectors.toList()))))
                    .map(converter::convert);
        } else if (!fromAccount.isPresent() && toAccount.isPresent()) {
            accountRepository.saveAndFlush(toAccount.get().setBalance(toAccount.get().getBalance() + amount));
            return Optional.of(transactionRepository.saveAndFlush(new Transaction()
                            .setAmount(amount)
                            .setDateAndTime(LocalDateTime.now())
                            .setFromAccount(null)
                            .setToAccount(toAccount.get())
                            .setCategoryTransaction(categoryTransactionId.stream()
                                    .map(categoryTransactionRepository::getById)
                                    .collect(Collectors.toList()))))
                    .map(converter::convert);
        }
        return Optional.empty();
    }
}
