package main.java.com.model1.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Locale;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import main.java.com.model1.app.AppContext;
import main.java.com.model1.app.SceneRouter;
import main.java.com.model1.model.dto.BillItemRequest;
import main.java.com.model1.model.entity.Bill;
import main.java.com.model1.model.entity.BillItem;
import main.java.com.model1.service.BillingService;
import main.java.com.model1.service.InventoryService;
import main.java.com.model1.service.ReceiptService;
import main.java.com.model1.util.ValidationUtils;

public class BillController {
    @FXML
    private ComboBox<String> productComboBox;
    @FXML
    private TextField quantityField;
    @FXML
    private ComboBox<Integer> sectorComboBox;
    @FXML
    private TableView<BillLineRow> billTable;
    @FXML
    private TableColumn<BillLineRow, String> productColumn;
    @FXML
    private TableColumn<BillLineRow, String> quantityColumn;
    @FXML
    private TableColumn<BillLineRow, String> unitPriceColumn;
    @FXML
    private TableColumn<BillLineRow, String> lineTotalColumn;
    @FXML
    private TableColumn<BillLineRow, String> sectorColumn;
    @FXML
    private Label runningTotalLabel;
    @FXML
    private Label itemCountLabel;
    @FXML
    private Label statusLabel;
    @FXML
    private Button removeItemButton;
    @FXML
    private Button clearBillButton;
    @FXML
    private Button finishBillButton;

    private final BillingService billingService = new BillingService();
    private final InventoryService inventoryService = new InventoryService();
    private final ReceiptService receiptService = new ReceiptService();
    private final ObservableList<BillLineRow> rows = FXCollections.observableArrayList();
    private final ObservableList<String> productNames = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        configureProducts();
        configureSectors();
        configureTable();
        billTable.setItems(rows);
        billTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        billTable.setPlaceholder(emptyState("No items added yet."));
        configureActionStates();
        updateSummary();
        setStatus("Ready to build a bill.", false);
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
    private void focusSector() {
        sectorComboBox.requestFocus();
    }

    @FXML
    private void addBillItem() {
        try {
            String productName = productName();
            int quantity = positiveInt(quantityField, "Quantity");
            int sector = selectedSector(sectorComboBox, "Sector");
            BillItem item = billingService.prepareItem(new BillItemRequest(productName, quantity, sector));
            rows.add(new BillLineRow(item));
            quantityField.clear();
            quantityField.requestFocus();
            updateSummary();
            setStatus(item.productName() + " added to the bill.", false);
        } catch (RuntimeException ex) {
            setStatus(ex.getMessage(), true);
        }
    }

    @FXML
    private void removeSelectedItem() {
        BillLineRow selected = billTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            setStatus("Select a bill line first.", true);
            return;
        }
        rows.remove(selected);
        updateSummary();
        setStatus(selected.item().productName() + " removed from the bill.", false);
    }

    @FXML
    private void clearBill() {
        rows.clear();
        clearInputs();
        updateSummary();
        setStatus("Current bill cleared.", false);
    }

    @FXML
    private void finishBill() {
        try {
            List<BillItem> items = rows.stream()
                    .map(BillLineRow::item)
                    .toList();
            ValidationUtils.requireNotEmpty(items, "Bill items");

            String cashierName = AppContext.getInstance().getActiveUsername().orElse("modern-cashier");
            String receiptFileName = receiptService.createReceiptFileName();
            String storedReceiptPath = receiptService.storedReceiptPath(receiptFileName);
            Bill bill = billingService.finishBill(cashierName, items, storedReceiptPath);
            receiptService.writeReceipt(bill);
            rows.clear();
            clearInputs();
            updateSummary();
            setStatus("Bill #" + bill.id() + " completed and receipt generated.", false);
        } catch (RuntimeException ex) {
            setStatus(ex.getMessage(), true);
        }
    }

    private void configureActionStates() {
        removeItemButton.disableProperty().bind(billTable.getSelectionModel().selectedItemProperty().isNull());
        clearBillButton.disableProperty().bind(Bindings.isEmpty(rows));
        finishBillButton.disableProperty().bind(Bindings.isEmpty(rows));
    }

    private void configureProducts() {
        try {
            productNames.setAll(inventoryService.findAllProductNames());
        } catch (RuntimeException ex) {
            productNames.clear();
        }
        productComboBox.setItems(productNames);
        productComboBox.setEditable(true);
        productComboBox.getEditor().textProperty().addListener((observable, previous, current) -> filterProducts(current));
    }

    private void configureSectors() {
        sectorComboBox.setItems(FXCollections.observableArrayList(1, 2, 3));
        sectorComboBox.getSelectionModel().selectFirst();
    }

    private void filterProducts(String query) {
        String normalizedQuery = query == null ? "" : query.trim().toLowerCase(Locale.ROOT);
        if (normalizedQuery.isBlank()) {
            productComboBox.setItems(productNames);
            return;
        }

        List<String> filtered = productNames.stream()
                .filter(name -> name.toLowerCase(Locale.ROOT).contains(normalizedQuery))
                .limit(25)
                .toList();
        productComboBox.setItems(FXCollections.observableArrayList(filtered));
        if (!filtered.isEmpty()) {
            productComboBox.show();
        }
    }

    private void configureTable() {
        productColumn.setCellValueFactory(data -> data.getValue().productProperty());
        quantityColumn.setCellValueFactory(data -> data.getValue().quantityProperty());
        unitPriceColumn.setCellValueFactory(data -> data.getValue().unitPriceProperty());
        lineTotalColumn.setCellValueFactory(data -> data.getValue().lineTotalProperty());
        sectorColumn.setCellValueFactory(data -> data.getValue().sectorProperty());
    }

    private void clearInputs() {
        productComboBox.getEditor().clear();
        quantityField.clear();
        sectorComboBox.getSelectionModel().selectFirst();
        productComboBox.requestFocus();
    }

    private String productName() {
        String value = productComboBox.getEditor().getText();
        ValidationUtils.requireNonBlank(value, "Product name");
        return value.trim();
    }

    private int positiveInt(TextField field, String name) {
        String value = field == null ? "" : field.getText();
        ValidationUtils.requireNonBlank(value, name);
        try {
            int parsed = Integer.parseInt(value.trim());
            ValidationUtils.requirePositive(parsed, name);
            return parsed;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(name + " must be a whole number.", ex);
        }
    }

    private int selectedSector(ComboBox<Integer> comboBox, String name) {
        Integer value = comboBox == null ? null : comboBox.getValue();
        ValidationUtils.requireNonNull(value, name);
        ValidationUtils.requirePositive(value, name);
        return value;
    }

    private void updateSummary() {
        int totalItems = rows.stream()
                .map(BillLineRow::item)
                .mapToInt(BillItem::quantity)
                .sum();
        BigDecimal total = rows.stream()
                .map(BillLineRow::item)
                .map(BillItem::lineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        itemCountLabel.setText(totalItems + " items");
        runningTotalLabel.setText(money(total));
    }

    private void setStatus(String message, boolean error) {
        statusLabel.setText(message == null ? "" : message);
        statusLabel.getStyleClass().removeAll("status-error", "status-success");
        statusLabel.getStyleClass().add(error ? "status-error" : "status-success");
    }

    private String money(BigDecimal value) {
        return "$" + value.setScale(2, RoundingMode.HALF_UP);
    }

    private SceneRouter router() {
        return AppContext.getInstance()
                .getSceneRouter()
                .orElseThrow(() -> new IllegalStateException("SceneRouter has not been initialized."));
    }

    private Label emptyState(String message) {
        Label label = new Label(message);
        label.getStyleClass().add("empty-state");
        label.setWrapText(true);
        return label;
    }

    public final class BillLineRow {
        private final BillItem item;
        private final StringProperty product;
        private final StringProperty quantity;
        private final StringProperty unitPrice;
        private final StringProperty lineTotal;
        private final StringProperty sector;

        private BillLineRow(BillItem item) {
            this.item = item;
            this.product = new SimpleStringProperty(item.productName());
            this.quantity = new SimpleStringProperty(String.valueOf(item.quantity()));
            this.unitPrice = new SimpleStringProperty(money(item.unitPrice()));
            this.lineTotal = new SimpleStringProperty(money(item.lineTotal()));
            this.sector = new SimpleStringProperty(String.valueOf(item.sector()));
        }

        private BillItem item() {
            return item;
        }

        private StringProperty productProperty() {
            return product;
        }

        private StringProperty quantityProperty() {
            return quantity;
        }

        private StringProperty unitPriceProperty() {
            return unitPrice;
        }

        private StringProperty lineTotalProperty() {
            return lineTotal;
        }

        private StringProperty sectorProperty() {
            return sector;
        }
    }
}
