package ru.piskunov.web.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.piskunov.web.converter.AccountModelToAccountDTOConverter;
import ru.piskunov.web.entity.Account;
import ru.piskunov.web.repository.AccountRepository;
import ru.piskunov.web.repository.UserRepository;
import ru.piskunov.web.service.dto.AccountDTO;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final AccountModelToAccountDTOConverter converter;

    @Transactional
    public List<AccountDTO> findAll(long userId) {
        return accountRepository.findAllByUserId(userId).stream().map(converter::convert)
                .collect(Collectors.toList());
    }

    @Transactional
    public Optional<AccountDTO> create(Long userId, String accountName) {
        if (!accountRepository.findByAccountNameAndUserId(accountName, userId).isPresent()) {
            return Optional.of(accountRepository.save(new Account()
                    .setAccountName(accountName)
                    .setUser(userRepository.getById(userId))
                    .setBalance(0L))).map(converter::convert);
        }
        return Optional.empty();
    }

    @Transactional
    public Optional<AccountDTO> rename(String newName, Long id, Long userId) {
        Optional<Account> checkAccount = accountRepository.findByIdAndUserId(id, userId);
        if (checkAccount.isPresent()) {
            if (!accountRepository.findByAccountNameAndUserId(newName, userId).isPresent()) {
                return Optional.of(accountRepository.saveAndFlush(checkAccount.get().setAccountName(newName))).map(converter::convert);
            }
        }
        return Optional.empty();
    }

    @Transactional
    public boolean delete(Long accountId, Long userId) {
        if (accountRepository.findByIdAndUserId(accountId, userId).isPresent()) {
            accountRepository.deleteAccountModelByUserIdAndId(userId, accountId);
            return true;
        }
        return false;
    }
}