package ru.piskunov.web.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import ru.piskunov.web.converter.TransactionModelToTransactionDTOConverter;
import ru.piskunov.web.entity.Account;
import ru.piskunov.web.entity.CategoryTransaction;
import ru.piskunov.web.entity.Transaction;
import ru.piskunov.web.entity.User;
import ru.piskunov.web.repository.AccountRepository;
import ru.piskunov.web.repository.CategoryTransactionRepository;
import ru.piskunov.web.repository.TransactionRepository;
import ru.piskunov.web.service.dto.TransactionDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceTest {

    @InjectMocks
    TransactionService subj;

    @Mock
    TransactionRepository transactionRepository;

    @Mock
    AccountRepository accountRepository;

    @Mock
    CategoryTransactionRepository categoryTransactionRepository;

    @Mock
    TransactionModelToTransactionDTOConverter converter;

    @Test
    public void create_TransactionFound() {
        LocalDateTime now = LocalDateTime.now();
        User user = new User().setId(1L);
        List<Long> list = asList(1L, 2L);
        CategoryTransaction categoryTransaction1 = new CategoryTransaction().setId(1L).setUser(user);
        CategoryTransaction categoryTransaction2 = new CategoryTransaction().setId(2L).setUser(user);
        Account fromAccount = new Account().setId(1L).setBalance(6000L).setUser(user);
        Account toAccount = new Account().setId(2L).setBalance(1000L).setUser(user);
        List<CategoryTransaction> categoryTransactions = asList(categoryTransaction1, categoryTransaction2);
        when(accountRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findByIdAndUserId(2L, 1L)).thenReturn(Optional.of(toAccount));
        when(categoryTransactionRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(categoryTransaction1));
        when(categoryTransactionRepository.findByIdAndUserId(2L, 1L)).thenReturn(Optional.of(categoryTransaction2));
        when(categoryTransactionRepository.getById(1L)).thenReturn(categoryTransaction1);
        when(categoryTransactionRepository.getById(2L)).thenReturn(categoryTransaction2);
        Transaction transactionModel = new Transaction()
                .setAmount(2000L)
                .setDateAndTime(now)
                .setFromAccount(fromAccount)
                .setToAccount(toAccount)
                .setCategoryTransaction(categoryTransactions);
        when(transactionRepository.saveAndFlush(new Transaction()
                .setAmount(2000L)
                .setDateAndTime(now.minusNanos(LocalDateTime.now().getNano()))
                .setFromAccount(fromAccount)
                .setToAccount(toAccount)
                .setCategoryTransaction(categoryTransactions)))
                .thenReturn(transactionModel);
        TransactionDTO transactionDTO = new TransactionDTO();
        when(converter.convert(transactionModel)).thenReturn(transactionDTO);

        Optional<TransactionDTO> transaction = subj.create(1L, 2000L, 1L, 2L, list);

        assertTrue(transaction.isPresent());
        assertEquals(transactionDTO, transaction.get());

        verify(transactionRepository, times(1)).saveAndFlush(new Transaction()
                .setAmount(2000L)
                .setDateAndTime(now)
                .setFromAccount(fromAccount)
                .setToAccount(toAccount)
                .setCategoryTransaction(categoryTransactions));
        verify(converter, times(1)).convert(transactionModel);
        verify(accountRepository, times(1)).findByIdAndUserId(1L, 1L);
        verify(accountRepository, times(1)).findByIdAndUserId(2L, 1L);
        verify(categoryTransactionRepository, times(1)).findByIdAndUserId(1L, 1L);
        verify(categoryTransactionRepository, times(1)).findByIdAndUserId(2L, 1L);
        verify(categoryTransactionRepository, times(1)).getById(1L);
        verify(categoryTransactionRepository, times(1)).getById(2L);
    }

    @Test
    public void create_TransactionNotFound() {
        List<Long> list = new ArrayList<>();
        Account fromAccount = new Account().setId(1L).setBalance(1000L);
        when(accountRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(fromAccount));

        assertFalse(subj.create(1L, 2000L, 1L, 2L, list).isPresent());

        verify(accountRepository, times(1)).findByIdAndUserId(1L, 1L);
        verifyNoInteractions(categoryTransactionRepository);
        verifyNoInteractions(converter);
        verifyNoInteractions(transactionRepository);
    }
}