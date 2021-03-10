package com.global.controller;

import com.global.model.TransactionStatistics;
import com.global.service.StatisticsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

 
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@RestController
public class TransactionStatisticsControler {
 
	@Autowired
    private StatisticsService<TransactionStatistics> transactionStatisticsService;

    @RequestMapping(value = "/statistics", method = {RequestMethod.GET}, produces = "application/json")
    private ResponseEntity<TransactionStatistics> statistics() {

        Instant instantOfRequest = Instant.now();

        TransactionStatistics transactionStatistics = transactionStatisticsService.
        		validPeriod(instantOfRequest.minus(1, ChronoUnit.MINUTES),
                        instantOfRequest);

        return new ResponseEntity<>(transactionStatistics, HttpStatus.OK);
    }
}
