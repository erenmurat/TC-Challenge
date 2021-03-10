package com.global.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.global.model.Transaction;
import com.global.repository.TransactionRepository;
import com.global.repository.TransactionRepositoryImpl;

@SpringBootTest
public class TransactionServiceTest {

    private TransactionRepository transactionRepository;

    private TransactionService transactionService;

    @Before
    public void init() {
        transactionService = new TransactionService();
        transactionRepository = new TransactionRepositoryImpl();
        transactionService.setTransactionRepository(transactionRepository);
    }

    @Test
    public void testSaveTransactionByServiceGetItFromRepositorySuccessfully() {
        Instant now = Instant.now();
        transactionService.saveTransaction(new Transaction(BigDecimal.valueOf(200), now));
        transactionService.saveTransaction(new Transaction(BigDecimal.valueOf(203), now));
        List<Transaction> transactionList = transactionService.findTransactionsByInstant(now);
        Assert.assertNotNull("Null List", transactionList);
        Assert.assertEquals("Amount is not equal to200", transactionList.get(0).getAmountDecimal(),
                BigDecimal.valueOf(200).setScale(2, BigDecimal.ROUND_HALF_UP));
        Assert.assertEquals("Amount is not equal to 203", transactionList.get(1).getAmountDecimal(),
                BigDecimal.valueOf(203).setScale(2, BigDecimal.ROUND_HALF_UP));
    }

    @Test
    public void testDeleteTransactionsByServiceSuccessfully() {
        Instant now = Instant.now();
        transactionService.saveTransaction(new Transaction(BigDecimal.valueOf(200), now));
        transactionService.saveTransaction(new Transaction(BigDecimal.valueOf(203), now));
        transactionService.removeTransactions();
        List<Transaction> transactionList = transactionService.findTransactionsByInstant(now);
        Assert.assertNull("List is not Null", transactionList);
    }

 

    @Test
    public void testOlderTransactionTimestampParamIn60SecRangeSuccessfully() {
        Instant now = Instant.now();
        Instant sixtyOneSecondBefore = now.minus(59, ChronoUnit.SECONDS);
        Transaction transaction = new Transaction(BigDecimal.valueOf(200), sixtyOneSecondBefore);
        boolean validationResponse = transactionService.validateForOlderTransactionTimestamp(transaction, now);
        Assert.assertTrue("Transaction older scope", validationResponse);
    }

    @Test
    public void testFutureTransactionTimestampParamIn60SecSuccesfully() {
        Instant now = Instant.now();
        Transaction transaction = new Transaction(BigDecimal.valueOf(200), now);
        boolean validationResponse = transactionService.validateForFutureTransactionTimestamp(transaction, now);
        Assert.assertTrue("Transaction future scope", validationResponse);
    }

    @Test
    public void testForFutureTransactionTimestampInFutureFailed() {
        Instant now = Instant.now();
        Transaction transaction = new Transaction(BigDecimal.valueOf(200), now.plus(5, ChronoUnit.SECONDS));
        boolean validationResponse = transactionService.validateForFutureTransactionTimestamp(transaction, now);
        Assert.assertFalse("Transaction is valid range", validationResponse);
    }

    @Test
    public void testTransactionAmountParam200Succesfully() {
        Transaction transaction = new Transaction();
        transaction.setAmount("200");
        boolean validationResponse = transactionService.validateTransactionAmount(transaction);
        Assert.assertTrue("Wrong Amount ", validationResponse);
    }

    @Test
    public void testTransactionAmountFailed() {
        Transaction transaction = new Transaction();
        transaction.setAmount("Test");
        boolean validationResponse = transactionService.validateTransactionAmount(transaction);
        Assert.assertFalse("Correct Amount ", validationResponse);
    }
}
