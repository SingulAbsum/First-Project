package main.java.com.model1.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SalesReport(
        LocalDate startDate,
        LocalDate endDate,
        int totalItemsSold,
        int totalItemsPurchased,
        BigDecimal salesRevenue,
        BigDecimal purchaseExpense,
        BigDecimal balance) {
}
