package main.java.com.model1.model.entity;

import java.math.BigDecimal;

public record Product(
        String name,
        int quantity,
        BigDecimal price,
        int supplierId,
        int sector) {
}
