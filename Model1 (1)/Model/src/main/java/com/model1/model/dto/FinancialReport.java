package main.java.com.model1.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FinancialReport(
        LocalDate startDate,
        LocalDate endDate,
        BigDecimal salesRevenue,
        BigDecimal purchaseExpense,
        BigDecimal salaryExpense,
        BigDecimal totalExpense,
        BigDecimal balance) {
}
