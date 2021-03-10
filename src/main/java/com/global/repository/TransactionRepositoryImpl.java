package com.global.repository;

import java.lang.ref.SoftReference;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.global.model.Transaction;

@Repository 
public class TransactionRepositoryImpl implements TransactionRepository {

    private final ConcurrentNavigableMap<Long, SoftReference<List<Transaction>>> transactionMap = new ConcurrentSkipListMap<>();
 

    @Override
    public void save(Transaction transaction) {
        transactionMap.computeIfAbsent(transaction.getTimestamp().toEpochMilli(),
                k -> {
                    SoftReference<List<Transaction>> ref = new SoftReference<>(Collections.synchronizedList(new ArrayList<>()));
                   return ref;
                }).get().add(transaction);

    }

  

    @Override
    public void deleteAll() {
       
        transactionMap.clear();
    }

    @Override
    public List<Transaction> findAllBetweenTimestamps(Instant from, Instant to) {

        if (from.isAfter(to))
            return null;
        return transactionMap
                .subMap(from.toEpochMilli(), to.toEpochMilli())
                .values()
                .parallelStream()
                .flatMap(i -> {
                    synchronized (i) {
                        return i.get().stream();
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Transaction> findTransactionsByInstant(Instant instant) {
        SoftReference<List<Transaction>> nullable = transactionMap.get(instant.toEpochMilli());
        return nullable != null ? nullable.get() : null;

    }

   
}
