package com.global.controller;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.global.model.Transaction;
import com.global.service.TransactionService;

 
@RestController
public class TransactionController {

	@Autowired
	private TransactionService transactionService;

	@RequestMapping(value = "/transactions", method = { RequestMethod.POST }, produces = "application/json")
	private ResponseEntity<Void> putTransaction(@RequestBody Transaction transaction) {
		Instant now = Instant.now();

		if (!transactionService.validateForOlderTransactionTimestamp(transaction, now))
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		else if (!transactionService.validateForFutureTransactionTimestamp(transaction, now))
			return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
		else if (!transactionService.validateTransactionAmount(transaction))
			return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
		else {
			transactionService.saveTransaction(transaction);
			return new ResponseEntity<>(HttpStatus.CREATED);
		}
	}

	@RequestMapping(value = "/transactions", method = { RequestMethod.DELETE }, produces = "application/json")
	private ResponseEntity<Void> removeTransactions() {
		transactionService.removeTransactions();
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);

	}

}
