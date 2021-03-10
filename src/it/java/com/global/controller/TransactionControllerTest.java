package com.global.controller;

import com.global.model.Transaction;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TransactionControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void test_transcations_withRandomDataReturnCreated() {

        Transaction transaction = new Transaction(BigDecimal.valueOf(199).setScale(2, RoundingMode.HALF_UP).toString(),
                Instant.now());

        ResponseEntity<Void> response = testRestTemplate.postForEntity("/transactions", transaction, Void.class);
        Assert.assertEquals("Response is not CREATED ", HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void testTranscationsPostWithRandomdataReturnNoContent() {

        Transaction transaction = new Transaction(BigDecimal.valueOf(199).setScale(2, RoundingMode.HALF_UP).toString(),
                Instant.now().minus(120, ChronoUnit.SECONDS));

        ResponseEntity<Void> response = testRestTemplate.postForEntity("/transactions", transaction, Void.class);
        Assert.assertEquals("Response is not NO_CONTENT ", HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void test_transcationsDeleteWithRandomDataReturnsNoContent() {

        ResponseEntity<Void> response = testRestTemplate.exchange("/transactions", HttpMethod.DELETE, null, Void.class);
        Assert.assertEquals("Response is not NO_CONTENT ", HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void test_transcationsPostWorngDataReturnBadReq() {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        ResponseEntity<Void> response = testRestTemplate.exchange("/transactions", HttpMethod.POST,
                new HttpEntity<>("Test", headers), Void.class);
        Assert.assertEquals("Response is not BAD_REQUEST ", HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

}
