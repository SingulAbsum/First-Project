package main.java.com.model1.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Locale;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import main.java.com.model1.app.AppContext;
import main.java.com.model1.app.SceneRouter;
import main.java.com.model1.model.entity.Product;
import main.java.com.model1.model.entity.Supplier;
import main.java.com.model1.service.InventoryService;
import main.java.com.model1.util.ValidationUtils;

public class InventoryController {
    @FXML
    private TextField searchField;
    @FXML
    private TextField productNameField;
    @FXML
    private TextField quantityField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField sectorField;
    @FXML
    private ComboBox<SupplierOption> supplierComboBox;
    @FXML
    private TextField refillQuantityField;
    @FXML
    private TextField newSupplierNameField;
    @FXML
    private TextField newSupplierCategoryField;
    @FXML
    private TableView<ProductRow> productTable;
    @FXML
    private TableColumn<ProductRow, String> productColumn;
    @FXML
    private TableColumn<ProductRow, String> stockColumn;
    @FXML
    private TableColumn<ProductRow, String> priceColumn;
    @FXML
    private TableColumn<ProductRow, String> supplierColumn;
    @FXML
    private TableColumn<ProductRow, String> sectorColumn;
    @FXML
    private TableColumn<ProductRow, String> statusColumn;
    @FXML
    private Label statusLabel;

    private final InventoryService inventoryService = new InventoryService();
    private final ObservableList<ProductRow> products = FXCollections.observableArrayList();
    private final ObservableList<SupplierOption> suppliers = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        configureTable();
        productTable.setItems(products);
        supplierComboBox.setItems(suppliers);
        searchField.textProperty().addListener((observable, previous, current) -> refreshProducts());
        productTable.getSelectionModel().selectedItemProperty().addListener((observable, previous, selected) -> populateSelection(selected));
        refreshSuppliers();
        refreshProducts();
    }

    @FXML
    private void backToDashboard() {
        router().showActiveDashboard();
    }

    @FXML
    private void signOut() {
        router().showLogin();
    }

    @FXML
    private void refreshData() {
        refreshSuppliers();
        refreshProducts();
    }

    @FXML
    private void addProduct() {
        try {
            SupplierOption supplier = selectedSupplier();
            Product product = new Product(
                    text(productNameField, "Product name"),
                    positiveInt(quantityField, "Product quantity"),
                    moneyValue(priceField, "Product price"),
                    supplier.supplier().id(),
                    positiveInt(sectorField, "Sector"));
            inventoryService.addProduct(product);
            clearProductInputs();
            refreshProducts();
            setStatus(product.name() + " added and purchase expense recorded.", false);
        } catch (RuntimeException ex) {
            logFailure("add product", ex);
            setStatus(ex.getMessage(), true);
        }
    }

    @FXML
    private void addCategory() {
        try {
            String supplierName = text(newSupplierNameField, "New supplier name");
            String supplierCategory = text(newSupplierCategoryField, "New supplier category");
            String productName = text(productNameField, "Product name");
            int quantity = positiveInt(quantityField, "Product quantity");
            BigDecimal price = moneyValue(priceField, "Product price");
            int sector = positiveInt(sectorField, "Sector");

            inventoryService.addProductCategory(supplierName, supplierCategory, productName, quantity, price, sector);
            clearProductInputs();
            newSupplierNameField.clear();
            newSupplierCategoryField.clear();
            refreshSuppliers();
            refreshProducts();
            setStatus(productName + " category added with supplier " + supplierName + ".", false);
        } catch (RuntimeException ex) {
            logFailure("add category", ex);
            setStatus(ex.getMessage(), true);
        }
    }

    @FXML
    private void refillProduct() {
        try {
            String productName = text(productNameField, "Product name");
            int sector = positiveInt(sectorField, "Sector");
            int quantity = positiveInt(refillQuantityField, "Refill quantity");
            inventoryService.refillProduct(productName, quantity, sector);
            refillQuantityField.clear();
            refreshProducts();
            setStatus(productName + " refilled. Expense recorded and matching notification resolved.", false);
        } catch (RuntimeException ex) {
            logFailure("refill product", ex);
            setStatus(ex.getMessage(), true);
        }
    }

    private void configureTable() {
        productColumn.setCellValueFactory(data -> data.getValue().productProperty());
        stockColumn.setCellValueFactory(data -> data.getValue().stockProperty());
        priceColumn.setCellValueFactory(data -> data.getValue().priceProperty());
        supplierColumn.setCellValueFactory(data -> data.getValue().supplierProperty());
        sectorColumn.setCellValueFactory(data -> data.getValue().sectorProperty());
        statusColumn.setCellValueFactory(data -> data.getValue().statusProperty());
        productTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void refreshProducts() {
        try {
            String filter = searchField == null ? "" : searchField.getText().trim().toLowerCase(Locale.ROOT);
            List<ProductRow> rows = inventoryService.findAllProducts().stream()
                    .filter(product -> filter.isBlank()
                            || product.name().toLowerCase(Locale.ROOT).contains(filter)
                            || String.valueOf(product.sector()).contains(filter)
                            || String.valueOf(product.supplierId()).contains(filter))
                    .map(ProductRow::new)
                    .toList();
            products.setAll(rows);
            setStatus(rows.size() + " products loaded.", false);
        } catch (RuntimeException ex) {
            logFailure("refresh products", ex);
            setStatus(ex.getMessage(), true);
        }
    }

    private void refreshSuppliers() {
        try {
            suppliers.setAll(inventoryService.findAllSuppliers().stream()
                    .map(SupplierOption::new)
                    .toList());
            if (!suppliers.isEmpty() && supplierComboBox.getSelectionModel().isEmpty()) {
                supplierComboBox.getSelectionModel().selectFirst();
            }
        } catch (RuntimeException ex) {
            logFailure("refresh suppliers", ex);
            setStatus(ex.getMessage(), true);
        }
    }

    private void populateSelection(ProductRow selected) {
        if (selected == null) {
            return;
        }
        Product product = selected.product();
        productNameField.setText(product.name());
        quantityField.setText(String.valueOf(product.quantity()));
        priceField.setText(product.price().setScale(2, RoundingMode.HALF_UP).toPlainString());
        sectorField.setText(String.valueOf(product.sector()));
        suppliers.stream()
                .filter(option -> option.supplier().id() == product.supplierId())
                .findFirst()
                .ifPresent(option -> supplierComboBox.getSelectionModel().select(option));
    }

    private SupplierOption selectedSupplier() {
        SupplierOption selected = supplierComboBox.getSelectionModel().getSelectedItem();
        ValidationUtils.requireNonNull(selected, "Supplier");
        return selected;
    }

    private String text(TextField field, String name) {
        return ValidationUtils.requireNonBlank(field == null ? "" : field.getText(), name);
    }

    private int positiveInt(TextField field, String name) {
        String value = text(field, name);
        try {
            int parsed = Integer.parseInt(value.trim());
            ValidationUtils.requirePositive(parsed, name);
            return parsed;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(name + " must be a whole number.", ex);
        }
    }

    private BigDecimal moneyValue(TextField field, String name) {
        String value = text(field, name);
        try {
            BigDecimal parsed = new BigDecimal(value.trim());
            ValidationUtils.requireNonNegative(parsed, name);
            return parsed;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(name + " must be a valid amount.", ex);
        }
    }

    private void clearProductInputs() {
        productNameField.clear();
        quantityField.clear();
        priceField.clear();
        sectorField.clear();
    }

    private String money(BigDecimal value) {
        return "$" + value.setScale(2, RoundingMode.HALF_UP);
    }

    private void setStatus(String message, boolean error) {
        statusLabel.setText(message == null ? "" : message);
        statusLabel.getStyleClass().removeAll("status-error", "status-success");
        statusLabel.getStyleClass().add(error ? "status-error" : "status-success");
    }

    private void logFailure(String action, Throwable throwable) {
        System.err.println("[Model1] Inventory action failed: " + action);
        throwable.printStackTrace(System.err);
    }

    private SceneRouter router() {
        return AppContext.getInstance()
                .getSceneRouter()
                .orElseThrow(() -> new IllegalStateException("SceneRouter has not been initialized."));
    }

    public final class ProductRow {
        private final Product product;
        private final StringProperty productName;
        private final StringProperty stock;
        private final StringProperty price;
        private final StringProperty supplier;
        private final StringProperty sector;
        private final StringProperty status;

        private ProductRow(Product product) {
            this.product = product;
            this.productName = new SimpleStringProperty(product.name());
            this.stock = new SimpleStringProperty(String.valueOf(product.quantity()));
            this.price = new SimpleStringProperty(money(product.price()));
            this.supplier = new SimpleStringProperty(String.valueOf(product.supplierId()));
            this.sector = new SimpleStringProperty(String.valueOf(product.sector()));
            this.status = new SimpleStringProperty(inventoryService.isLowStock(product) ? "Low stock" : "In stock");
        }

        private Product product() {
            return product;
        }

        private StringProperty productProperty() {
            return productName;
        }

        private StringProperty stockProperty() {
            return stock;
        }

        private StringProperty priceProperty() {
            return price;
        }

        private StringProperty supplierProperty() {
            return supplier;
        }

        private StringProperty sectorProperty() {
            return sector;
        }

        private StringProperty statusProperty() {
            return status;
        }
    }

    public record SupplierOption(Supplier supplier) {
        @Override
        public String toString() {
            return supplier.id() + " - " + supplier.name() + " / " + supplier.category();
        }
    }
}
