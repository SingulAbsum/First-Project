package main.java.com.model1.model.entity;

import java.math.BigDecimal;

public record BillItem(
        String productName,
        int quantity,
        BigDecimal unitPrice,
        int sector) {
    public BigDecimal lineTotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
