package main.java.com.model1.service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import main.java.com.model1.exception.DatabaseException;
import main.java.com.model1.exception.ValidationException;
import main.java.com.model1.model.entity.Expense;
import main.java.com.model1.model.entity.Notification;
import main.java.com.model1.model.entity.Product;
import main.java.com.model1.model.entity.Supplier;
import main.java.com.model1.repository.DatabaseManager;
import main.java.com.model1.repository.ExpenseRepository;
import main.java.com.model1.repository.NotificationRepository;
import main.java.com.model1.repository.ProductRepository;
import main.java.com.model1.repository.SupplierRepository;
import main.java.com.model1.util.ValidationUtils;

public class InventoryService {
    private static final int LOW_STOCK_THRESHOLD = 5;

    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;
    private final ExpenseRepository expenseRepository;
    private final NotificationRepository notificationRepository;

    public InventoryService() {
        this(new ProductRepository(), new SupplierRepository(), new ExpenseRepository(), new NotificationRepository());
    }

    public InventoryService(
            ProductRepository productRepository,
            SupplierRepository supplierRepository,
            ExpenseRepository expenseRepository,
            NotificationRepository notificationRepository) {
        this.productRepository = productRepository;
        this.supplierRepository = supplierRepository;
        this.expenseRepository = expenseRepository;
        this.notificationRepository = notificationRepository;
    }

    public List<Product> findAllProducts() {
        try {
            return productRepository.findAll();
        } catch (SQLException ex) {
            throw new DatabaseException("Unable to load products.", ex);
        }
    }

    public List<String> findAllProductNames() {
        try {
            return productRepository.findAllNames();
        } catch (SQLException ex) {
            throw new DatabaseException("Unable to load product names.", ex);
        }
    }

    public List<Supplier> findAllSuppliers() {
        try {
            return supplierRepository.findAll();
        } catch (SQLException ex) {
            throw new DatabaseException("Unable to load suppliers.", ex);
        }
    }

    public Product findProduct(String productName, int sector) {
        ValidationUtils.requireNonBlank(productName, "Product name");

        try {
            return productRepository.findByNameAndSector(productName, sector)
                    .orElseThrow(() -> new ValidationException("Product not found: " + productName));
        } catch (SQLException ex) {
            throw new DatabaseException("Unable to load product.", ex);
        }
    }

    public void addProduct(Product product) {
        validateProduct(product);

        DatabaseManager.inTransaction(connection -> {
            productRepository.save(connection, product);
            expenseRepository.save(connection, purchaseExpense(product));
        });
    }

    public void addProductCategory(Product product, Supplier supplier) {
        validateProduct(product);
        validateSupplier(supplier);

        DatabaseManager.inTransaction(connection -> {
            supplierRepository.save(connection, supplier);
            productRepository.save(connection, product);
            expenseRepository.save(connection, purchaseExpense(product));
        });
    }

    public void addProductCategory(String supplierName, String supplierCategory, String productName, int quantity, BigDecimal price, int sector) {
        ValidationUtils.requireNonBlank(supplierName, "Supplier name");
        ValidationUtils.requireNonBlank(supplierCategory, "Supplier category");

        DatabaseManager.inTransaction(connection -> {
            int supplierId = supplierRepository.nextSupplierId(connection);
            Supplier supplier = new Supplier(supplierId, supplierName, supplierCategory);
            Product product = new Product(productName, quantity, price, supplierId, sector);
            validateSupplier(supplier);
            validateProduct(product);
            supplierRepository.save(connection, supplier);
            productRepository.save(connection, product);
            expenseRepository.save(connection, purchaseExpense(product));
        });
    }

    public Supplier addSupplierCategory(String supplierName, String supplierCategory) {
        ValidationUtils.requireNonBlank(supplierName, "Supplier name");
        ValidationUtils.requireNonBlank(supplierCategory, "Supplier category");

        return DatabaseManager.inTransaction(connection -> {
            int supplierId = supplierRepository.nextSupplierId(connection);
            Supplier supplier = new Supplier(supplierId, supplierName, supplierCategory);
            validateSupplier(supplier);
            supplierRepository.save(connection, supplier);
            return supplier;
        });
    }

    public void refillProduct(String productName, int quantity, int sector) {
        ValidationUtils.requireNonBlank(productName, "Product name");
        ValidationUtils.requirePositive(quantity, "Refill quantity");

        DatabaseManager.inTransaction(connection -> {
            Product product = productRepository.findByNameAndSector(connection, productName, sector)
                    .orElseThrow(() -> new ValidationException("Product not found: " + productName));
            productRepository.increaseQuantity(connection, productName, quantity, sector);
            BigDecimal amount = product.price().multiply(BigDecimal.valueOf(quantity));
            expenseRepository.save(connection, new Expense(LocalDate.now(), quantity, amount, sector));
            notificationRepository.delete(connection, new Notification(productName, sector));
        });
    }

    public boolean isLowStock(Product product) {
        ValidationUtils.requireNonNull(product, "Product");
        return product.quantity() <= LOW_STOCK_THRESHOLD;
    }

    private void validateProduct(Product product) {
        ValidationUtils.requireNonNull(product, "Product");
        ValidationUtils.requireNonBlank(product.name(), "Product name");
        ValidationUtils.requirePositive(product.quantity(), "Product quantity");
        ValidationUtils.requireNonNegative(product.price(), "Product price");
        ValidationUtils.requirePositive(product.supplierId(), "Supplier id");
    }

    private void validateSupplier(Supplier supplier) {
        ValidationUtils.requireNonNull(supplier, "Supplier");
        ValidationUtils.requirePositive(supplier.id(), "Supplier id");
        ValidationUtils.requireNonBlank(supplier.name(), "Supplier name");
        ValidationUtils.requireNonBlank(supplier.category(), "Supplier category");
    }

    private Expense purchaseExpense(Product product) {
        BigDecimal amount = product.price().multiply(BigDecimal.valueOf(product.quantity()));
        return new Expense(LocalDate.now(), product.quantity(), amount, product.sector());
    }
}
