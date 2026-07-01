package main.java.com.model1.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

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
import main.java.com.model1.model.dto.CashierReport;
import main.java.com.model1.model.dto.FinancialReport;
import main.java.com.model1.model.dto.SalesReport;
import main.java.com.model1.service.ReportService;
import main.java.com.model1.util.DateParser;
import main.java.com.model1.util.ValidationUtils;

public class ReportController {
    @FXML
    private BorderPane rootPane;
    @FXML
    private TextField cashierField;
    @FXML
    private TextField startDateField;
    @FXML
    private TextField endDateField;
    @FXML
    private Label reportTypeLabel;
    @FXML
    private Label revenueLabel;
    @FXML
    private Label expenseLabel;
    @FXML
    private Label balanceLabel;
    @FXML
    private TableView<ReportRow> reportTable;
    @FXML
    private TableColumn<ReportRow, String> metricColumn;
    @FXML
    private TableColumn<ReportRow, String> valueColumn;
    @FXML
    private TableColumn<ReportRow, String> contextColumn;
    @FXML
    private Label statusLabel;

    private final ReportService reportService = new ReportService();
    private final ObservableList<ReportRow> rows = FXCollections.observableArrayList();
    private ReportRoute route = ReportRoute.MANAGER_REPORTS;

    @FXML
    private void initialize() {
        Object value = rootPane.getUserData();
        if (value != null) {
            route = ReportRoute.valueOf(value.toString());
        }
        configureTable();
        setDefaultDates();
        reportTypeLabel.setText(route.displayName);
        if (route == ReportRoute.ADMIN_FINANCIAL_REPORTS && cashierField != null) {
            cashierField.setDisable(true);
            cashierField.setPromptText("Not used for financial reports");
        }
        renderEmptyState();
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
    private void generateSalesReport() {
        try {
            SalesReport report = reportService.salesReport(startDate(), endDate());
            rows.setAll(
                    new ReportRow("Items sold", String.valueOf(report.totalItemsSold()), dateRange(report.startDate(), report.endDate())),
                    new ReportRow("Items purchased", String.valueOf(report.totalItemsPurchased()), dateRange(report.startDate(), report.endDate())),
                    new ReportRow("Sales revenue", money(report.salesRevenue()), "Bill totals"),
                    new ReportRow("Purchase expense", money(report.purchaseExpense()), "Inventory expenses"),
                    new ReportRow("Balance", money(report.balance()), "Revenue minus purchases"));
            updateSummary("Manager sales report", report.salesRevenue(), report.purchaseExpense(), report.balance());
            setStatus("Sales report generated.", false);
        } catch (RuntimeException ex) {
            logFailure("sales report", ex);
            setStatus(ex.getMessage(), true);
        }
    }

    @FXML
    private void generateCashierReport() {
        try {
            String cashierName = ValidationUtils.requireNonBlank(cashierField.getText(), "Cashier name");
            CashierReport report = reportService.cashierReport(cashierName, startDate(), endDate());
            rows.setAll(
                    new ReportRow("Bills generated", String.valueOf(report.totalBills()), report.cashierName()),
                    new ReportRow("Items sold", String.valueOf(report.totalItemsSold()), report.cashierName()),
                    new ReportRow("Revenue", money(report.totalRevenue()), dateRange(report.startDate(), report.endDate())));
            updateSummary("Cashier report", report.totalRevenue(), BigDecimal.ZERO, report.totalRevenue());
            setStatus("Cashier report generated for " + report.cashierName() + ".", false);
        } catch (RuntimeException ex) {
            logFailure("cashier report", ex);
            setStatus(ex.getMessage(), true);
        }
    }

    @FXML
    private void generateFinancialReport() {
        try {
            FinancialReport report = reportService.financialReport(startDate(), endDate());
            rows.setAll(
                    new ReportRow("Revenue", money(report.salesRevenue()), "Bill totals"),
                    new ReportRow("Purchases", money(report.purchaseExpense()), "Stock expense"),
                    new ReportRow("Salaries", money(report.salaryExpense()), "Payroll"),
                    new ReportRow("Total expense", money(report.totalExpense()), "Purchases plus salaries"),
                    new ReportRow("Balance", money(report.balance()), dateRange(report.startDate(), report.endDate())));
            updateSummary("Financial report", report.salesRevenue(), report.totalExpense(), report.balance());
            setStatus("Financial report generated.", false);
        } catch (RuntimeException ex) {
            logFailure("financial report", ex);
            setStatus(ex.getMessage(), true);
        }
    }

    private void configureTable() {
        metricColumn.setCellValueFactory(data -> data.getValue().metricProperty());
        valueColumn.setCellValueFactory(data -> data.getValue().valueProperty());
        contextColumn.setCellValueFactory(data -> data.getValue().contextProperty());
        reportTable.setItems(rows);
        reportTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void setDefaultDates() {
        LocalDate today = LocalDate.now();
        startDateField.setText(today.withDayOfMonth(1).toString());
        endDateField.setText(today.toString());
    }

    private LocalDate startDate() {
        return DateParser.parseIsoOrLegacy(startDateField.getText(), "Start date");
    }

    private LocalDate endDate() {
        return DateParser.parseIsoOrLegacy(endDateField.getText(), "End date");
    }

    private void renderEmptyState() {
        rows.setAll(new ReportRow("Report ready", "Enter dates and generate", "yyyy-MM-dd or MMddyyyy"));
        updateSummary(route.displayName, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
        setStatus("Date range defaults to the current month.", false);
    }

    private void updateSummary(String reportType, BigDecimal revenue, BigDecimal expense, BigDecimal balance) {
        reportTypeLabel.setText(reportType);
        revenueLabel.setText(money(revenue));
        expenseLabel.setText(money(expense));
        balanceLabel.setText(money(balance));
    }

    private String dateRange(LocalDate startDate, LocalDate endDate) {
        return startDate + " to " + endDate;
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
        System.err.println("[Model1] Report action failed: " + action);
        throwable.printStackTrace(System.err);
    }

    private SceneRouter router() {
        return AppContext.getInstance()
                .getSceneRouter()
                .orElseThrow(() -> new IllegalStateException("SceneRouter has not been initialized."));
    }

    public static final class ReportRow {
        private final StringProperty metric;
        private final StringProperty value;
        private final StringProperty context;

        private ReportRow(String metric, String value, String context) {
            this.metric = new SimpleStringProperty(metric);
            this.value = new SimpleStringProperty(value);
            this.context = new SimpleStringProperty(context);
        }

        private StringProperty metricProperty() {
            return metric;
        }

        private StringProperty valueProperty() {
            return value;
        }

        private StringProperty contextProperty() {
            return context;
        }
    }

    private enum ReportRoute {
        MANAGER_REPORTS("Manager reports"),
        ADMIN_FINANCIAL_REPORTS("Financial reports");

        private final String displayName;

        ReportRoute(String displayName) {
            this.displayName = displayName;
        }
    }
}
