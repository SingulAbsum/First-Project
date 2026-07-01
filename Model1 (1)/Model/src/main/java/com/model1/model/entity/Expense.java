package main.java.com.model1.model.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

public record Expense(
        LocalDate date,
        int itemsPurchased,
        BigDecimal amount,
        int sector) {
}
