package main.java.com.model1.model.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record Bill(
        int id,
        LocalDate date,
        BigDecimal total,
        String fileName,
        String cashierName,
        int totalItems,
        List<BillItem> items) {
}
