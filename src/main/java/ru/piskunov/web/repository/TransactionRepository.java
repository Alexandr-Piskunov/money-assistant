package ru.piskunov.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.piskunov.web.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long>, TransactionRepositoryCustom {

}
