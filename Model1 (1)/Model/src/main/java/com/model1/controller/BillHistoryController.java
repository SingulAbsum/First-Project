package main.java.com.model1.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import main.java.com.model1.app.AppContext;
import main.java.com.model1.app.SceneRouter;
import main.java.com.model1.model.entity.Bill;
import main.java.com.model1.model.entity.BillItem;
import main.java.com.model1.service.BillHistoryService;
import main.java.com.model1.service.ReceiptService;
import main.java.com.model1.util.DateParser;

public class BillHistoryController {
    @FXML
    private DatePicker datePicker;
    @FXML
    private TableView<BillHistoryRow> billTable;
    @FXML
    private TableColumn<BillHistoryRow, String> idColumn;
    @FXML
    private TableColumn<BillHistoryRow, String> dateColumn;
    @FXML
    private TableColumn<BillHistoryRow, String> cashierColumn;
    @FXML
    private TableColumn<BillHistoryRow, String> itemsColumn;
    @FXML
    private TableColumn<BillHistoryRow, String> totalColumn;
    @FXML
    private TableColumn<BillHistoryRow, String> receiptColumn;
    @FXML
    private TableView<BillItemRow> itemTable;
    @FXML
    private TableColumn<BillItemRow, String> productColumn;
    @FXML
    private TableColumn<BillItemRow, String> quantityColumn;
    @FXML
    private TableColumn<BillItemRow, String> unitPriceColumn;
    @FXML
    private TableColumn<BillItemRow, String> lineTotalColumn;
    @FXML
    private TableColumn<BillItemRow, String> sectorColumn;
    @FXML
    private Label statusLabel;
    @FXML
    private Button openReceiptButton;
    @FXML
    private Button regenerateReceiptButton;

    private final BillHistoryService billHistoryService = new BillHistoryService();
    private final ReceiptService receiptService = new ReceiptService();
    private final ObservableList<BillHistoryRow> bills = FXCollections.observableArrayList();
    private final ObservableList<BillItemRow> items = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        configureBillTable();
        configureItemTable();
        billTable.setItems(bills);
        itemTable.setItems(items);
        configureActionStates();
        billTable.getSelectionModel().selectedItemProperty().addListener((observable, previous, selected) -> showItems(selected));
        loadAllHistory();
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
    private void loadAllHistory() {
        try {
            renderBills(billHistoryService.findAllBills(), "All saved bills loaded.");
        } catch (RuntimeException ex) {
            setStatus(ex.getMessage(), true);
        }
    }

    @FXML
    private void loadHistoryForDate() {
        try {
            LocalDate date = selectedDate(datePicker, "History date");
            renderBills(billHistoryService.findBillsByDate(date), "Bills loaded for " + date + ".");
        } catch (RuntimeException ex) {
            setStatus(ex.getMessage(), true);
        }
    }

    @FXML
    private void openReceipt() {
        try {
            Bill bill = selectedBill();
            receiptService.openReceipt(bill);
            setStatus("Opened receipt for bill #" + bill.id() + ".", false);
        } catch (RuntimeException ex) {
            logFailure("open receipt", ex);
            setStatus(ex.getMessage(), true);
        }
    }

    @FXML
    private void regenerateReceipt() {
        try {
            Bill bill = selectedBill();
            receiptService.regenerateReceipt(bill);
            setStatus("Regenerated receipt for bill #" + bill.id() + " from saved bill items.", false);
        } catch (RuntimeException ex) {
            logFailure("regenerate receipt", ex);
            setStatus(ex.getMessage(), true);
        }
    }

    private void configureBillTable() {
        idColumn.setCellValueFactory(data -> data.getValue().idProperty());
        dateColumn.setCellValueFactory(data -> data.getValue().dateProperty());
        cashierColumn.setCellValueFactory(data -> data.getValue().cashierProperty());
        itemsColumn.setCellValueFactory(data -> data.getValue().itemsProperty());
        totalColumn.setCellValueFactory(data -> data.getValue().totalProperty());
        receiptColumn.setCellValueFactory(data -> data.getValue().receiptProperty());
        billTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        billTable.setPlaceholder(emptyState("No bills found for this selection."));
    }

    private void configureItemTable() {
        productColumn.setCellValueFactory(data -> data.getValue().productProperty());
        quantityColumn.setCellValueFactory(data -> data.getValue().quantityProperty());
        unitPriceColumn.setCellValueFactory(data -> data.getValue().unitPriceProperty());
        lineTotalColumn.setCellValueFactory(data -> data.getValue().lineTotalProperty());
        sectorColumn.setCellValueFactory(data -> data.getValue().sectorProperty());
        itemTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        itemTable.setPlaceholder(emptyState("Select a bill to inspect saved items."));
    }

    private void configureActionStates() {
        openReceiptButton.disableProperty().bind(billTable.getSelectionModel().selectedItemProperty().isNull());
        regenerateReceiptButton.disableProperty().bind(billTable.getSelectionModel().selectedItemProperty().isNull());
    }

    private LocalDate selectedDate(DatePicker picker, String fieldName) {
        if (picker != null && picker.getValue() != null) {
            return picker.getValue();
        }
        String typedValue = picker == null ? "" : picker.getEditor().getText();
        return DateParser.parseIsoOrLegacy(typedValue, fieldName);
    }

    private void renderBills(List<Bill> loadedBills, String message) {
        bills.setAll(loadedBills.stream().map(BillHistoryRow::new).toList());
        items.clear();
        if (!bills.isEmpty()) {
            billTable.getSelectionModel().selectFirst();
        }
        setStatus(bills.size() + " bills. " + message, false);
    }

    private void showItems(BillHistoryRow selected) {
        if (selected == null) {
            items.clear();
            return;
        }
        items.setAll(selected.bill().items().stream().map(BillItemRow::new).toList());
        if (items.isEmpty()) {
            setStatus("Bill #" + selected.bill().id() + " has no saved line-item details yet.", false);
        }
    }

    private Bill selectedBill() {
        BillHistoryRow selected = billTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            throw new IllegalArgumentException("Select a bill first.");
        }
        return selected.bill();
    }

    private void setStatus(String message, boolean error) {
        statusLabel.setText(message == null ? "" : message);
        statusLabel.getStyleClass().removeAll("status-error", "status-success");
        statusLabel.getStyleClass().add(error ? "status-error" : "status-success");
    }

    private void logFailure(String action, Throwable throwable) {
        System.err.println("[Model1] Receipt action failed: " + action);
        throwable.printStackTrace(System.err);
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

    public final class BillHistoryRow {
        private final Bill bill;
        private final StringProperty id;
        private final StringProperty date;
        private final StringProperty cashier;
        private final StringProperty items;
        private final StringProperty total;
        private final StringProperty receipt;

        private BillHistoryRow(Bill bill) {
            this.bill = bill;
            this.id = new SimpleStringProperty(String.valueOf(bill.id()));
            this.date = new SimpleStringProperty(String.valueOf(bill.date()));
            this.cashier = new SimpleStringProperty(bill.cashierName());
            this.items = new SimpleStringProperty(String.valueOf(bill.totalItems()));
            this.total = new SimpleStringProperty(money(bill.total()));
            this.receipt = new SimpleStringProperty(bill.fileName());
        }

        private Bill bill() {
            return bill;
        }

        private StringProperty idProperty() {
            return id;
        }

        private StringProperty dateProperty() {
            return date;
        }

        private StringProperty cashierProperty() {
            return cashier;
        }

        private StringProperty itemsProperty() {
            return items;
        }

        private StringProperty totalProperty() {
            return total;
        }

        private StringProperty receiptProperty() {
            return receipt;
        }
    }

    public final class BillItemRow {
        private final StringProperty product;
        private final StringProperty quantity;
        private final StringProperty unitPrice;
        private final StringProperty lineTotal;
        private final StringProperty sector;

        private BillItemRow(BillItem item) {
            this.product = new SimpleStringProperty(item.productName());
            this.quantity = new SimpleStringProperty(String.valueOf(item.quantity()));
            this.unitPrice = new SimpleStringProperty(money(item.unitPrice()));
            this.lineTotal = new SimpleStringProperty(money(item.lineTotal()));
            this.sector = new SimpleStringProperty(String.valueOf(item.sector()));
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
