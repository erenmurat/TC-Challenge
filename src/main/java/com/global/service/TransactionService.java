package com.global.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

  
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.global.model.Transaction;
import com.global.repository.TransactionRepository;

@Service("TransactionService")
public class TransactionService {

  	@Autowired
    private TransactionRepository transactionRepository;

    public boolean validateForOlderTransactionTimestamp(Transaction transaction, Instant now) {
        return transaction.getTimestamp().isAfter(now.minus(60, ChronoUnit.SECONDS));
    }

    public boolean validateForFutureTransactionTimestamp(Transaction transaction, Instant now) {
        return !transaction.getTimestamp().isAfter(now);
    }

    public List<Transaction> findTransactionsByInstant(Instant instant) {
        return transactionRepository.findTransactionsByInstant(instant);
    }

    public void saveTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    public void removeTransactions() {
        transactionRepository.deleteAll();
    }

    public boolean validateTransactionAmount(Transaction transaction) {
        try {
            transaction.setAmountDecimal(new BigDecimal(transaction.getAmount()));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void setTransactionRepository(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

}
