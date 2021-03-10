package com.global.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.global.model.Transaction;
import com.global.model.TransactionStatistics;
import com.global.repository.TransactionRepository;
import com.global.repository.TransactionRepositoryImpl;

@SpringBootTest
public class TransactionStatisticsServiceTest {

	private TransactionStatisticsService transactionStatisticsStatisticsService;

	private TransactionRepository transactionRepository;

	@Before
	public void initialize() {
		transactionStatisticsStatisticsService = new TransactionStatisticsService();
		transactionRepository = new TransactionRepositoryImpl();
		transactionStatisticsStatisticsService.setTransactionRepository(transactionRepository);
	}

	@Test
	public void testTransactionStatisticsOfaTimePeriodWithoutTransactionsReturnsEmptytObject() {
		Instant now = Instant.now();

		TransactionStatistics emptyTransactionStatistics = new TransactionStatistics();

		TransactionStatistics transactionStatistics = transactionStatisticsStatisticsService
				.validPeriod(now.minus(1, ChronoUnit.MINUTES), now);

		Assert.assertEquals("Transaction is not Null", emptyTransactionStatistics, transactionStatistics);
	}

	@Test
	public void testTransactionStatisticsOfaTimePeriodWithTransactionDataOnRepositoryReturnsObject() {
		Instant now = Instant.now();
		transactionRepository.deleteAll();
		fillRepositoryWithObjects(now);
		TransactionStatistics predefinedTransactionStatistics = new TransactionStatistics();
		predefinedTransactionStatistics.setSum("2370.00");
		predefinedTransactionStatistics.setAvg("39.50");
		predefinedTransactionStatistics.setMax("69.00");
		predefinedTransactionStatistics.setMin("10.00");
		predefinedTransactionStatistics.setCount(60);

		TransactionStatistics transactionStatistics = transactionStatisticsStatisticsService
				.validPeriod(now.minus(1, ChronoUnit.MINUTES), now);

		Assert.assertEquals("Transaction not equals to Values", predefinedTransactionStatistics, transactionStatistics);
	}

	private void fillRepositoryWithObjects(Instant now) {
		for (int i = 120; i >= 0; i--) {
			transactionRepository
					.save(new Transaction(BigDecimal.valueOf(10 + 1 * i), now.minus(1 + i, ChronoUnit.SECONDS)));
		}
	}
}
