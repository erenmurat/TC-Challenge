package com.global.repository;

import com.global.model.Transaction;

import java.time.Instant;
import java.util.List;

public interface TransactionRepository {

    void save(Transaction transaction);

    void deleteAll();

    List<Transaction> findAllBetweenTimestamps(Instant from, Instant to);

    List<Transaction> findTransactionsByInstant(Instant instant);
}
