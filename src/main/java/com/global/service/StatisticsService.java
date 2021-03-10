package com.global.service;

import java.time.Instant;

public interface StatisticsService<TransactionStatistics> {

	TransactionStatistics validPeriod(Instant start, Instant end);
}
