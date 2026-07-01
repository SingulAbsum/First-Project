package main.java.com.model1.model.dto;

public record BillItemRequest(
        String productName,
        int quantity,
        int sector) {
}
