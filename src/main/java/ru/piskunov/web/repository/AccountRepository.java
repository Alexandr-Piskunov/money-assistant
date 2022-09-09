package ru.piskunov.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.piskunov.web.entity.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findAllByUserId(Long userId);

    Optional<Account> findByIdAndUserId(Long id, Long userId);

    Optional<Account> findByAccountNameAndUserId(String accountName, Long userId);

    void deleteAccountModelByUserIdAndId(Long userId, Long id);
}
