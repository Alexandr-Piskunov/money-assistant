package ru.piskunov.web.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.piskunov.web.entity.Transaction;

import java.sql.Date;
import java.util.List;

import static org.junit.Assert.*;

@DataJpaTest
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TransactionRepositoryTest {

    @Autowired
    TransactionRepository subj;

    @Test
    public void findByFilter_transactionFound() {
        TransactionFilter transactionFilter = new TransactionFilter().setUserId(1L)
                .setStartDate(Date.valueOf("2022-06-05"))
                .setEndDate(Date.valueOf("2022-06-06"));

        List<Transaction> transactions = subj.findByFilter(transactionFilter);
        assertNotNull(transactions);
        assertEquals(1, transactions.size());
    }

    @Test
    public void findByFilter_transactionNoTFound() {
        TransactionFilter transactionFilter = new TransactionFilter().setUserId(1L)
                .setStartDate(Date.valueOf("2022-06-05"))
                .setEndDate(Date.valueOf("2022-06-05"));

        List<Transaction> transactions = subj.findByFilter(transactionFilter);
        assertNotNull(transactions);
        assertTrue(transactions.isEmpty());
    }

    @Test
    public void findByFilter_userIdNotFound() {
        TransactionFilter transactionFilter = new TransactionFilter().setUserId(1L)
                .setStartDate(Date.valueOf("2022-06-05"))
                .setEndDate(Date.valueOf("2022-06-05"));

        List<Transaction> transactions = subj.findByFilter(transactionFilter);
        assertTrue(transactions.isEmpty());
    }

    @Test
    public void findByFilter_endDateNotFound() {
        TransactionFilter transactionFilter = new TransactionFilter().setUserId(1L)
                .setStartDate(Date.valueOf("2022-06-05"));

        List<Transaction> transactions = subj.findByFilter(transactionFilter);
        assertNotNull(transactions);
        assertEquals(1, transactions.size());
    }
}