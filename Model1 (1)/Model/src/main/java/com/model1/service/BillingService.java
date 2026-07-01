package main.java.com.model1.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import main.java.com.model1.exception.InsufficientStockException;
import main.java.com.model1.exception.ValidationException;
import main.java.com.model1.model.dto.BillItemRequest;
import main.java.com.model1.model.entity.Bill;
import main.java.com.model1.model.entity.BillItem;
import main.java.com.model1.model.entity.Notification;
import main.java.com.model1.model.entity.Product;
import main.java.com.model1.repository.BillItemRepository;
import main.java.com.model1.repository.BillRepository;
import main.java.com.model1.repository.DatabaseManager;
import main.java.com.model1.repository.NotificationRepository;
import main.java.com.model1.repository.ProductRepository;
import main.java.com.model1.util.ValidationUtils;

public class BillingService {
    private static final int LOW_STOCK_THRESHOLD = 5;

    private final ProductRepository productRepository;
    private final BillRepository billRepository;
    private final BillItemRepository billItemRepository;
    private final NotificationRepository notificationRepository;

    public BillingService() {
        this(new ProductRepository(), new BillRepository(), new BillItemRepository(), new NotificationRepository());
    }

    public BillingService(
            ProductRepository productRepository,
            BillRepository billRepository,
            BillItemRepository billItemRepository,
            NotificationRepository notificationRepository) {
        this.productRepository = productRepository;
        this.billRepository = billRepository;
        this.billItemRepository = billItemRepository;
        this.notificationRepository = notificationRepository;
    }

    public BillItem prepareItem(BillItemRequest request) {
        ValidationUtils.requireNonNull(request, "Bill item request");
        ValidationUtils.requireNonBlank(request.productName(), "Product name");
        ValidationUtils.requirePositive(request.quantity(), "Quantity");

        return DatabaseManager.inTransaction(connection -> {
            Product product = productRepository.findByNameAndSector(connection, request.productName(), request.sector())
                    .orElseThrow(() -> new ValidationException("Product not found: " + request.productName()));
            if (product.quantity() < request.quantity()) {
                throw new InsufficientStockException("Insufficient stock for product: " + request.productName());
            }
            return new BillItem(product.name(), request.quantity(), product.price(), product.sector());
        });
    }

    public Bill finishBill(String cashierName, List<BillItem> items, String receiptFileName) {
        ValidationUtils.requireNonBlank(cashierName, "Cashier name");
        ValidationUtils.requireNotEmpty(items, "Bill items");
        ValidationUtils.requireNonBlank(receiptFileName, "Receipt file name");

        return DatabaseManager.inTransaction(connection -> {
            int billId = billRepository.nextBillId(connection);
            Bill bill = new Bill(
                    billId,
                    LocalDate.now(),
                    calculateTotal(items),
                    receiptFileName,
                    cashierName,
                    calculateTotalItems(items),
                    List.copyOf(items));
            billRepository.save(connection, bill);
            billItemRepository.saveAll(connection, bill.id(), bill.items());

            for (BillItem item : items) {
                int updated = productRepository.decreaseQuantity(connection, item.productName(), item.quantity(), item.sector());
                if (updated != 1) {
                    throw new InsufficientStockException("Insufficient stock for product: " + item.productName());
                }
                Product remaining = productRepository.findByNameAndSector(connection, item.productName(), item.sector())
                        .orElseThrow(() -> new ValidationException("Product disappeared after update: " + item.productName()));
                if (remaining.quantity() <= LOW_STOCK_THRESHOLD) {
                    notificationRepository.save(connection, new Notification(item.productName(), item.sector()));
                }
            }
            return bill;
        });
    }

    public BigDecimal calculateTotal(List<BillItem> items) {
        ValidationUtils.requireNotEmpty(items, "Bill items");
        return items.stream()
                .map(BillItem::lineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public int calculateTotalItems(List<BillItem> items) {
        ValidationUtils.requireNotEmpty(items, "Bill items");
        return items.stream()
                .mapToInt(BillItem::quantity)
                .sum();
    }
}
