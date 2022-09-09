package ru.piskunov.web.repository;

import lombok.RequiredArgsConstructor;
import ru.piskunov.web.entity.Transaction;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;

@RequiredArgsConstructor
public class TransactionRepositoryImpl implements TransactionRepositoryCustom {
    private final EntityManager em;

    @Override
    public List<Transaction> findByFilter(TransactionFilter transactionFilter) {
        String query = "select t from Transaction t join t.fromAccount u where 1 = 1 ";

        Map<String, Object> params = new HashMap<>();

        if (transactionFilter.getUserId() == null) {
            return emptyList();
        }
        params.put("userId", transactionFilter.getUserId());
        transactionFilter.setStartDate(transactionFilter.getStartDate() == null ? Date.valueOf(LocalDate.now()) : transactionFilter.getStartDate());
        transactionFilter.setEndDate(transactionFilter.getEndDate() == null ? Date.valueOf(LocalDate.now()) : transactionFilter.getEndDate());
        query += " and u.user.id =:userId and cast(t.dateAndTime as date) between :startDate and :endDate ";
        params.put("startDate", transactionFilter.getStartDate());
        params.put("endDate", transactionFilter.getEndDate());

        TypedQuery<Transaction> typedQuery = em.createQuery(query, Transaction.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            typedQuery.setParameter(entry.getKey(), entry.getValue());
        }

        return typedQuery.getResultList();
    }
}
