package ru.piskunov.web.repository;

import ru.piskunov.web.entity.Transaction;

import java.util.List;

public interface TransactionRepositoryCustom {
    List<Transaction> findByFilter(TransactionFilter transactionFilter);
}
