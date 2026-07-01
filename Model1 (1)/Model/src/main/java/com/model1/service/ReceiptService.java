package main.java.com.model1.service;

import java.io.IOException;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import main.java.com.model1.exception.ReceiptGenerationException;
import main.java.com.model1.model.entity.Bill;
import main.java.com.model1.model.entity.BillItem;
import main.java.com.model1.util.ValidationUtils;

public class ReceiptService {
    private final Path receiptDirectory;

    public ReceiptService() {
        this(Path.of("."));
    }

    public ReceiptService(Path receiptDirectory) {
        this.receiptDirectory = receiptDirectory;
    }

    public Path writeReceipt(Bill bill) {
        ValidationUtils.requireNonNull(bill, "Bill");

        try {
            Files.createDirectories(receiptDirectory);
            Path receiptPath = receiptDirectory.resolve(bill.fileName());
            Files.write(receiptPath, renderReceipt(bill));
            return receiptPath;
        } catch (IOException ex) {
            throw new ReceiptGenerationException("Unable to write receipt file.", ex);
        }
    }

    public List<String> renderReceipt(Bill bill) {
        ValidationUtils.requireNonNull(bill, "Bill");
        ValidationUtils.requireNotEmpty(bill.items(), "Bill items");

        List<String> lines = new java.util.ArrayList<>();
        lines.add("Bill #" + bill.id());
        lines.add("Date: " + bill.date());
        lines.add("Cashier: " + bill.cashierName());
        lines.add("");
        for (BillItem item : bill.items()) {
            lines.add(item.productName() + " "
                    + item.quantity() + "x"
                    + item.unitPrice().setScale(2, RoundingMode.HALF_UP)
                    + " Total:"
                    + item.lineTotal().setScale(2, RoundingMode.HALF_UP));
        }
        lines.add("");
        lines.add("Total: " + bill.total().setScale(2, RoundingMode.HALF_UP));
        return lines;
    }
}
