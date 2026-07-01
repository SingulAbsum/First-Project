package main.java.com.model1.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CashierReport(
        String cashierName,
        LocalDate startDate,
        LocalDate endDate,
        int totalBills,
        int totalItemsSold,
        BigDecimal totalRevenue) {
}
