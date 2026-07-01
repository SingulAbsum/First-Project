package main.java.com.model1.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import main.java.com.model1.app.AppContext;
import main.java.com.model1.app.SceneRouter;
import main.java.com.model1.exception.DatabaseException;
import main.java.com.model1.exception.ValidationException;
import main.java.com.model1.model.dto.BillItemRequest;
import main.java.com.model1.model.dto.CashierReport;
import main.java.com.model1.model.dto.FinancialReport;
import main.java.com.model1.model.dto.SalesReport;
import main.java.com.model1.model.entity.Bill;
import main.java.com.model1.model.entity.BillItem;
import main.java.com.model1.model.entity.Employee;
import main.java.com.model1.model.entity.Notification;
import main.java.com.model1.model.entity.Product;
import main.java.com.model1.model.enums.Role;
import main.java.com.model1.service.BillingService;
import main.java.com.model1.service.EmployeeService;
import main.java.com.model1.service.InventoryService;
import main.java.com.model1.service.NotificationService;
import main.java.com.model1.service.ReceiptService;
import main.java.com.model1.service.ReportService;
import main.java.com.model1.util.DateParser;
import main.java.com.model1.util.ValidationUtils;

public class ModuleController {
    private static final DateTimeFormatter RECEIPT_DATE = DateTimeFormatter.ofPattern("MMddyyyy");

    @FXML
    private BorderPane rootPane;
    @FXML
    private Label routeLabel;
    @FXML
    private Label statusLabel;
    @FXML
    private TableView<ModuleRow> moduleTable;
    @FXML
    private TableColumn<ModuleRow, String> itemColumn;
    @FXML
    private TableColumn<ModuleRow, String> detailColumn;
    @FXML
    private TableColumn<ModuleRow, String> statusColumn;
    @FXML
    private TextField productField;
    @FXML
    private TextField quantityField;
    @FXML
    private TextField sectorField;
    @FXML
    private TextField searchField;
    @FXML
    private TextField startDateField;
    @FXML
    private TextField endDateField;
    @FXML
    private TextField cashierField;

    private final BillingService billingService = new BillingService();
    private final ReceiptService receiptService = new ReceiptService();
    private final InventoryService inventoryService = new InventoryService();
    private final NotificationService notificationService = new NotificationService();
    private final ReportService reportService = new ReportService();
    private final EmployeeService employeeService = new EmployeeService();
    private final ObservableList<ModuleRow> rows = FXCollections.observableArrayList();
    private final List<BillItem> currentBillItems = new ArrayList<>();
    private ModuleRoute route = ModuleRoute.CASHIER_BILL_EDITOR;

    @FXML
    private void initialize() {
        Object value = rootPane.getUserData();
        if (value != null) {
            route = ModuleRoute.valueOf(value.toString());
        }
        if (routeLabel != null) {
            routeLabel.setText(route.displayName);
        }
        configureTable();
        refreshData();
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
        try {
            switch (route) {
                case CASHIER_BILL_EDITOR -> refreshBillEditor();
                case CASHIER_BILL_HISTORY -> refreshBillHistory();
                case MANAGER_INVENTORY -> refreshInventory();
                case MANAGER_NOTIFICATIONS -> refreshNotifications();
                case MANAGER_REPORTS -> refreshReportsShell();
                case ADMIN_EMPLOYEES -> refreshEmployees();
                case ADMIN_FINANCIAL_REPORTS -> refreshFinancialShell();
            }
        } catch (DatabaseException ex) {
            setStatus("Database unavailable: " + ex.getMessage(), true);
        }
    }

    @FXML
    private void addBillItem() {
        try {
            String product = text(productField, "Product");
            int quantity = positiveInt(quantityField, "Quantity");
            int sector = positiveInt(sectorField, "Sector");
            BillItem item = billingService.prepareItem(new BillItemRequest(product, quantity, sector));
            currentBillItems.add(item);
            rows.add(new ModuleRow(
                    item.productName(),
                    item.quantity() + " x " + money(item.unitPrice()),
                    "Line total " + money(item.lineTotal())));
            setStatus("Item added to the current bill.", false);
        } catch (RuntimeException ex) {
            setStatus(ex.getMessage(), true);
        }
    }

    @FXML
    private void clearBill() {
        currentBillItems.clear();
        refreshBillEditor();
        setStatus("Current bill cleared.", false);
    }

    @FXML
    private void finishBill() {
        try {
            ValidationUtils.requireNotEmpty(currentBillItems, "Bill items");
            String cashierName = AppContext.getInstance().getActiveUsername().orElse("modern-cashier");
            String receiptFileName = "modern-" + System.currentTimeMillis() + "-" + RECEIPT_DATE.format(LocalDate.now()) + ".txt";
            Bill bill = billingService.finishBill(cashierName, currentBillItems, receiptFileName);
            receiptService.writeReceipt(bill);
            currentBillItems.clear();
            refreshBillEditor();
            setStatus("Bill #" + bill.id() + " completed and receipt generated.", false);
        } catch (RuntimeException ex) {
            setStatus(ex.getMessage(), true);
        }
    }

    @FXML
    private void addProduct() {
        showPreparedAction("Add product form is ready for Phase 11 service wiring.");
    }

    @FXML
    private void addCategory() {
        showPreparedAction("New category form is ready for Phase 11 service wiring.");
    }

    @FXML
    private void refillProduct() {
        showPreparedAction("Refill workflow is ready for Phase 11 service wiring.");
    }

    @FXML
    private void resolveSelected() {
        ModuleRow selected = selectedRow();
        if (selected == null) {
            setStatus("Select a notification first.", true);
            return;
        }
        setStatus("Resolve action selected for " + selected.item() + ".", false);
    }

    @FXML
    private void generateSalesReport() {
        try {
            LocalDate start = date(startDateField, "Start date");
            LocalDate end = date(endDateField, "End date");
            SalesReport report = reportService.salesReport(start, end);
            rows.setAll(
                    new ModuleRow("Items sold", String.valueOf(report.totalItemsSold()), "Selected range"),
                    new ModuleRow("Items purchased", String.valueOf(report.totalItemsPurchased()), "Selected range"),
                    new ModuleRow("Revenue", money(report.salesRevenue()), "Sales"),
                    new ModuleRow("Purchase expense", money(report.purchaseExpense()), "Expenses"),
                    new ModuleRow("Balance", money(report.balance()), "Net"));
            setStatus("Sales report generated.", false);
        } catch (RuntimeException ex) {
            setStatus(ex.getMessage(), true);
        }
    }

    @FXML
    private void generateCashierReport() {
        try {
            String cashierName = text(cashierField, "Cashier name");
            LocalDate start = date(startDateField, "Start date");
            LocalDate end = date(endDateField, "End date");
            CashierReport report = reportService.cashierReport(cashierName, start, end);
            rows.setAll(
                    new ModuleRow("Bills generated", String.valueOf(report.totalBills()), report.cashierName()),
                    new ModuleRow("Items sold", String.valueOf(report.totalItemsSold()), report.cashierName()),
                    new ModuleRow("Revenue", money(report.totalRevenue()), report.startDate() + " to " + report.endDate()));
            setStatus("Cashier report generated.", false);
        } catch (RuntimeException ex) {
            setStatus(ex.getMessage(), true);
        }
    }

    @FXML
    private void generateFinancialReport() {
        try {
            LocalDate start = date(startDateField, "Start date");
            LocalDate end = date(endDateField, "End date");
            FinancialReport report = reportService.financialReport(start, end);
            rows.setAll(
                    new ModuleRow("Revenue", money(report.salesRevenue()), "Income"),
                    new ModuleRow("Purchases", money(report.purchaseExpense()), "Stock expense"),
                    new ModuleRow("Salaries", money(report.salaryExpense()), "Payroll"),
                    new ModuleRow("Total expense", money(report.totalExpense()), "Combined"),
                    new ModuleRow("Balance", money(report.balance()), report.startDate() + " to " + report.endDate()));
            setStatus("Financial report generated.", false);
        } catch (RuntimeException ex) {
            setStatus(ex.getMessage(), true);
        }
    }

    @FXML
    private void addEmployee() {
        showPreparedAction("Add employee form is ready for Phase 13 service wiring.");
    }

    @FXML
    private void editEmployee() {
        showPreparedAction("Edit employee workflow is ready for Phase 13 service wiring.");
    }

    @FXML
    private void deleteEmployee() {
        showPreparedAction("Delete employee workflow is ready for Phase 13 service wiring.");
    }

    private void configureTable() {
        if (moduleTable == null) {
            return;
        }
        itemColumn.setCellValueFactory(cell -> cell.getValue().itemProperty());
        detailColumn.setCellValueFactory(cell -> cell.getValue().detailProperty());
        statusColumn.setCellValueFactory(cell -> cell.getValue().statusProperty());
        moduleTable.setItems(rows);
        moduleTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void refreshBillEditor() {
        rows.clear();
        if (currentBillItems.isEmpty()) {
            rows.add(new ModuleRow("Current bill", "No items yet", "Add an item to start"));
            setStatus("Ready to build a new bill.", false);
            return;
        }
        for (BillItem item : currentBillItems) {
            rows.add(new ModuleRow(
                    item.productName(),
                    item.quantity() + " x " + money(item.unitPrice()),
                    "Line total " + money(item.lineTotal())));
        }
        setStatus("Current total: " + money(billingService.calculateTotal(currentBillItems)), false);
    }

    private void refreshBillHistory() {
        rows.setAll(
                new ModuleRow("Bill history", "Date filtering surface", "Phase 10 repository expansion"),
                new ModuleRow("Receipt preview", "TableView ready", "Phase 14 receipt handling"));
        setStatus("History workspace ready.", false);
    }

    private void refreshInventory() {
        String filter = searchField == null ? "" : searchField.getText().trim().toLowerCase();
        List<ModuleRow> productRows = inventoryService.findAllProducts().stream()
                .filter(product -> filter.isBlank() || product.name().toLowerCase().contains(filter))
                .map(product -> new ModuleRow(
                        product.name(),
                        "Qty " + product.quantity() + " / " + money(product.price()),
                        "Supplier " + product.supplierId() + " / Sector " + product.sector()))
                .toList();
        rows.setAll(productRows);
        setStatus(productRows.size() + " products loaded.", false);
    }

    private void refreshNotifications() {
        List<ModuleRow> notificationRows = new ArrayList<>();
        for (int sector = 1; sector <= 3; sector++) {
            for (Notification notification : notificationService.findBySector(sector)) {
                notificationRows.add(new ModuleRow(
                        notification.productName(),
                        "Sector " + notification.sector(),
                        "Low stock signal"));
            }
        }
        rows.setAll(notificationRows);
        setStatus(notificationRows.size() + " notifications loaded.", false);
    }

    private void refreshReportsShell() {
        rows.setAll(
                new ModuleRow("Sales report", "Enter dates and generate", "Ready"),
                new ModuleRow("Cashier report", "Enter cashier and dates", "Ready"));
        setStatus("Report controls ready.", false);
    }

    private void refreshEmployees() {
        List<ModuleRow> employeeRows = new ArrayList<>();
        for (Role role : Role.values()) {
            for (Employee employee : employeeService.findByRole(role)) {
                employeeRows.add(new ModuleRow(
                        employee.getName(),
                        role.name() + " / " + money(employee.getSalary()),
                        "Access " + employee.getAccessLevel().value() + " / Sector " + employee.getSector()));
            }
        }
        rows.setAll(employeeRows);
        setStatus(employeeRows.size() + " employees loaded.", false);
    }

    private void refreshFinancialShell() {
        rows.setAll(
                new ModuleRow("Revenue", "Waiting for date range", "Ready"),
                new ModuleRow("Expenses", "Purchases and salaries", "Ready"),
                new ModuleRow("Balance", "Calculated after generation", "Ready"));
        setStatus("Financial report controls ready.", false);
    }

    private ModuleRow selectedRow() {
        return moduleTable == null ? null : moduleTable.getSelectionModel().getSelectedItem();
    }

    private void showPreparedAction(String message) {
        setStatus(message, false);
    }

    private String text(TextField field, String fieldName) {
        if (field == null) {
            throw new ValidationException(fieldName + " control is not available.");
        }
        return ValidationUtils.requireNonBlank(field.getText(), fieldName);
    }

    private int positiveInt(TextField field, String fieldName) {
        String value = text(field, fieldName);
        try {
            return ValidationUtils.requirePositive(Integer.parseInt(value), fieldName);
        } catch (NumberFormatException ex) {
            throw new ValidationException(fieldName + " must be a whole number.", ex);
        }
    }

    private LocalDate date(TextField field, String fieldName) {
        return DateParser.parseIsoOrLegacy(text(field, fieldName), fieldName);
    }

    private String money(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }

    private void setStatus(String message, boolean error) {
        if (statusLabel == null) {
            return;
        }
        statusLabel.setText(message == null ? "" : message);
        statusLabel.getStyleClass().removeAll("status-error", "status-success");
        statusLabel.getStyleClass().add(error ? "status-error" : "status-success");
    }

    private SceneRouter router() {
        return AppContext.getInstance()
                .getSceneRouter()
                .orElseThrow(() -> new IllegalStateException("SceneRouter has not been initialized."));
    }

    private enum ModuleRoute {
        CASHIER_BILL_EDITOR("Cashier / Bill Editor"),
        CASHIER_BILL_HISTORY("Cashier / Bill History"),
        MANAGER_INVENTORY("Manager / Inventory"),
        MANAGER_NOTIFICATIONS("Manager / Notifications"),
        MANAGER_REPORTS("Manager / Reports"),
        ADMIN_EMPLOYEES("Administrator / Employees"),
        ADMIN_FINANCIAL_REPORTS("Administrator / Financial Reports");

        private final String displayName;

        ModuleRoute(String displayName) {
            this.displayName = displayName;
        }
    }

    public static final class ModuleRow {
        private final StringProperty item;
        private final StringProperty detail;
        private final StringProperty status;

        ModuleRow(String item, String detail, String status) {
            this.item = new SimpleStringProperty(item);
            this.detail = new SimpleStringProperty(detail);
            this.status = new SimpleStringProperty(status);
        }

        public String item() {
            return item.get();
        }

        public StringProperty itemProperty() {
            return item;
        }

        public StringProperty detailProperty() {
            return detail;
        }

        public StringProperty statusProperty() {
            return status;
        }
    }
}
