package ru.piskunov.web.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import ru.piskunov.web.converter.AccountModelToAccountDTOConverter;
import ru.piskunov.web.entity.Account;
import ru.piskunov.web.entity.User;
import ru.piskunov.web.repository.AccountRepository;
import ru.piskunov.web.repository.UserRepository;
import ru.piskunov.web.service.dto.AccountDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {

    @InjectMocks
    AccountService subj;

    @Mock
    AccountRepository accountRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    AccountModelToAccountDTOConverter converter;

    @Test
    public void findAll_AccountFound() {
        User user = new User().setId(1L);
        List<Account> accounts = new ArrayList<>();
        accounts.add(new Account().setId(1L)
                .setAccountName("adc")
                .setBalance(0L)
                .setUser(user));
        when(accountRepository.findAllByUserId(1L)).thenReturn(accounts);
        AccountDTO accountDTO1 = new AccountDTO();
        when(converter.convert(accounts.get(0))).thenReturn(accountDTO1);
        List<AccountDTO> accountDTOS = new ArrayList<>();
        accountDTOS.add(accountDTO1);

        assertEquals(accountDTOS, subj.findAll(1));

        verify(accountRepository, times(1)).findAllByUserId(1L);
        verify(converter, times(1)).convert(accounts.get(0));
    }

    @Test
    public void findAll_AccountNotFound() {
        List<Account> accounts = new ArrayList<>();
        when(accountRepository.findAllByUserId(1L)).thenReturn(accounts);
        List<AccountDTO> accountDTOS = new ArrayList<>();

        assertEquals(accountDTOS, subj.findAll(1));

        verify(accountRepository, times(1)).findAllByUserId(1L);
        verifyNoInteractions(converter);
    }

    @Test
    public void create_accountFound() {
        User user = new User()
                .setId(1L);
        when(accountRepository.findByAccountNameAndUserId("newAccount", 1L)).thenReturn(Optional.empty());
        when(userRepository.getById(1L)).thenReturn(user);
        Account account = new Account()
                .setAccountName("newAccount")
                .setBalance(0L)
                .setUser(user);
        when(accountRepository.save(new Account()
                .setAccountName("newAccount")
                .setBalance(0L)
                .setUser(user))).thenReturn(account);
        AccountDTO accountDTO = new AccountDTO();
        when(converter.convert(account)).thenReturn(accountDTO);
        Optional<AccountDTO> newAccount = subj.create(1L, "newAccount");

        assertTrue(newAccount.isPresent());
        assertEquals(accountDTO, newAccount.get());

        verify(accountRepository, times(1)).save(new Account()
                .setAccountName("newAccount")
                .setBalance(0L)
                .setUser(user));
        verify(accountRepository, times(1)).findByAccountNameAndUserId("newAccount", 1L);
        verify(converter, times(1)).convert(account);
        verify(userRepository, times(1)).getById(1L);
    }

    @Test
    public void create_accountNotFound() {
        Account account = new Account();
        when(accountRepository.findByAccountNameAndUserId("newAccount", 1L)).thenReturn(Optional.of(account));

        assertFalse(subj.create(1L, "newAccount").isPresent());

        verify(accountRepository, times(1)).findByAccountNameAndUserId("newAccount", 1L);
        verifyNoInteractions(converter);
        verifyNoInteractions(userRepository);
    }

    @Test
    public void delete_Found() {
        Account account = new Account();
        when(accountRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(account));
        assertTrue(subj.delete(1L, 1L));

        verify(accountRepository, times(1)).findByIdAndUserId(1L, 1L);
        verify(accountRepository, times(1)).deleteAccountModelByUserIdAndId(1L, 1L);
        verifyNoInteractions(userRepository);
        verifyNoInteractions(converter);
    }

    @Test
    public void delete_Not_Found() {
        when(accountRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.empty());

        assertFalse(subj.delete(1L, 1L));

        verify(accountRepository, times(1)).findByIdAndUserId(1L, 1L);
        verifyNoInteractions(userRepository);
        verifyNoInteractions(converter);
    }

    @Test
    public void rename_AccountFound() {
        User user = new User().setId(1L);
        Account account = new Account()
                .setId(1L)
                .setBalance(0L)
                .setAccountName("NewTestName")
                .setUser(user);
        when(accountRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(account));
        when(accountRepository.findByAccountNameAndUserId("NewTestName", 1L)).thenReturn(Optional.empty());
        when(accountRepository.saveAndFlush(new Account()
                .setId(1L)
                .setBalance(0L)
                .setAccountName("NewTestName")
                .setUser(user))).thenReturn(account);
        AccountDTO accountDTO = new AccountDTO();
        when(converter.convert(account)).thenReturn(accountDTO);
        Optional<AccountDTO> newTestName = subj.rename("NewTestName", 1L, 1L);

        assertTrue(newTestName.isPresent());
        assertEquals(accountDTO, newTestName.get());

        verify(accountRepository, times(1)).findByIdAndUserId(1L, 1L);
        verify(accountRepository, times(1)).findByAccountNameAndUserId("NewTestName", 1L);
        verify(accountRepository, times(1)).saveAndFlush(new Account()
                .setId(1L)
                .setBalance(0L)
                .setAccountName("NewTestName")
                .setUser(user));
        verify(converter, times(1)).convert(account);
        verifyNoInteractions(userRepository);
    }

    @Test
    public void rename_AccountNotFound() {
        when(accountRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.empty());

        assertFalse(subj.rename("NewTestName", 1L, 1L).isPresent());

        verify(accountRepository, times(1)).findByIdAndUserId(1L, 1L);
        verifyNoInteractions(converter);
        verifyNoInteractions(userRepository);
    }
}