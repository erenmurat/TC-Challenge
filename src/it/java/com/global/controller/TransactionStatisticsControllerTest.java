package com.global.controller;

import com.global.model.Transaction;
import com.global.model.TransactionStatistics;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TransactionStatisticsControllerTest {

	private final static double VAL_200 = 200D;
	private final static double VAL_202 = 202D;
	private final static double VAL_204 = 204D;

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Test
	public void test_getStatisticsWithNoParamReturnsStatistics() {

		testRestTemplate.delete("/transactions");
		TransactionStatistics transactionStatistics = new TransactionStatistics();
     	getStatisticsData(transactionStatistics);
     	testRestTemplate.postForEntity("/transactions", new Transaction(
				BigDecimal.valueOf(VAL_200).setScale(2, RoundingMode.HALF_UP).toString(), Instant.now()), Void.class);
		testRestTemplate.postForEntity("/transactions", new Transaction(
				BigDecimal.valueOf(VAL_202).setScale(2, RoundingMode.HALF_UP).toString(), Instant.now()), Void.class);
		testRestTemplate.postForEntity("/transactions", new Transaction(
				BigDecimal.valueOf(VAL_204).setScale(2, RoundingMode.HALF_UP).toString(), Instant.now()), Void.class);

		ResponseEntity<TransactionStatistics> response = testRestTemplate.getForEntity("/statistics",
				TransactionStatistics.class);
		Assert.assertEquals(" NOK ", HttpStatus.OK, response.getStatusCode());
		Assert.assertEquals(" not expected ", transactionStatistics, response.getBody());

	}

	private void getStatisticsData(TransactionStatistics transactionStatistics) {
		transactionStatistics.setCount(3);
		transactionStatistics.setMin(BigDecimal.valueOf(200).setScale(2, RoundingMode.HALF_UP).toString());
		transactionStatistics.setMax(BigDecimal.valueOf(204).setScale(2, RoundingMode.HALF_UP).toString());
		transactionStatistics.setAvg(BigDecimal.valueOf(202).setScale(2, RoundingMode.HALF_UP).toString());
		transactionStatistics.setSum(BigDecimal.valueOf(606).setScale(2, RoundingMode.HALF_UP).toString());
	}
}
