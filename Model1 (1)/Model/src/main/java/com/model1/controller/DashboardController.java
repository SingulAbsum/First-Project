package main.java.com.model1.controller;

import java.util.List;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import main.java.com.model1.app.AppContext;
import main.java.com.model1.app.SceneRouter;
import main.java.com.model1.exception.DatabaseException;
import main.java.com.model1.model.entity.Product;
import main.java.com.model1.model.enums.Role;
import main.java.com.model1.service.InventoryService;
import main.java.com.model1.service.NotificationService;

public class DashboardController {
    @FXML
    private Label dashboardTitle;
    @FXML
    private Label dashboardSubtitle;
    @FXML
    private Label activeUserLabel;
    @FXML
    private Label heroKicker;
    @FXML
    private Label heroMetric;
    @FXML
    private Label heroCaption;
    @FXML
    private Label statProducts;
    @FXML
    private Label statLowStock;
    @FXML
    private Label statNotifications;
    @FXML
    private Label statMode;
    @FXML
    private Label statusLabel;
    @FXML
    private VBox primaryActions;
    @FXML
    private TableView<DashboardRow> previewTable;
    @FXML
    private TableColumn<DashboardRow, String> itemColumn;
    @FXML
    private TableColumn<DashboardRow, String> detailColumn;
    @FXML
    private TableColumn<DashboardRow, String> statusColumn;
    @FXML
    private Region scanLine;

    private final InventoryService inventoryService = new InventoryService();
    private final NotificationService notificationService = new NotificationService();
    private Timeline scanTimeline;

    @FXML
    private void initialize() {
        Role role = activeRole();
        configureTable();
        configureHeader(role);
        configureActions(role);
        configurePreviewRoutes(role);
        loadOperationalSnapshot(role);
        playScanAnimation();
    }

    @FXML
    private void handleSignOut() {
        router().showLogin();
    }

    private void configureTable() {
        itemColumn.setCellValueFactory(cell -> cell.getValue().itemProperty());
        detailColumn.setCellValueFactory(cell -> cell.getValue().detailProperty());
        statusColumn.setCellValueFactory(cell -> cell.getValue().statusProperty());
        previewTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void configureHeader(Role role) {
        String username = AppContext.getInstance().getActiveUsername().orElse("Operator");
        activeUserLabel.setText(username + " / " + displayRole(role));

        switch (role) {
            case CASHIER -> {
                dashboardTitle.setText("Cashier Flight Deck");
                dashboardSubtitle.setText("A fast billing surface with receipt flow one click away.");
                heroKicker.setText("Point of sale");
                heroMetric.setText("Checkout ready");
                heroCaption.setText("Create bills, review receipt history, and keep the checkout path focused.");
                statMode.setText("Cashier");
                previewTable.setItems(FXCollections.observableArrayList(
                        new DashboardRow("Create bill", "Open the modern bill editor scene", "Modern route"),
                        new DashboardRow("Bill history", "Open the modern bill history scene", "Modern route"),
                        new DashboardRow("Session", "Cashier login history remains tracked", "Connected")));
            }
            case MANAGER -> {
                dashboardTitle.setText("Inventory Observatory");
                dashboardSubtitle.setText("A live operations surface for stock, products, notifications, and reports.");
                heroKicker.setText("Manager console");
                heroMetric.setText("Stock aware");
                heroCaption.setText("Product creation, refill flows, and low-stock notifications remain connected to the existing data.");
                statMode.setText("Manager");
                previewTable.setItems(FXCollections.observableArrayList(
                        new DashboardRow("Inventory", "Open the modern inventory scene", "Modern route"),
                        new DashboardRow("Notifications", "Open the modern notification scene", "Modern route"),
                        new DashboardRow("Reports", "Open the modern report scene", "Modern route")));
            }
            case ADMINISTRATOR -> {
                dashboardTitle.setText("Operations Cortex");
                dashboardSubtitle.setText("A command layer for employees, access levels, salaries, and financial visibility.");
                heroKicker.setText("Admin console");
                heroMetric.setText("Control plane");
                heroCaption.setText("Employee management and finance reports are staged behind a modern dashboard shell.");
                statMode.setText("Administrator");
                previewTable.setItems(FXCollections.observableArrayList(
                        new DashboardRow("Employees", "Open the modern employee management scene", "Modern route"),
                        new DashboardRow("Access", "Role-scoped permissions stay intact", "Ready"),
                        new DashboardRow("Finance", "Open the modern financial report scene", "Modern route")));
            }
        }
    }

    private void configureActions(Role role) {
        primaryActions.getChildren().clear();

        switch (role) {
            case CASHIER -> {
                primaryActions.getChildren().add(actionButton("Create new bill", "Open the cashier bill editor", () -> openRoute("bill editor", () -> router().openCashierBillEditor())));
                primaryActions.getChildren().add(actionButton("View bill history", "Review saved receipt statistics", () -> openRoute("bill history", () -> router().openCashierBillHistory())));
            }
            case MANAGER -> {
                primaryActions.getChildren().add(actionButton("Add product", "Register stock for an existing supplier", () -> openRoute("inventory", () -> router().openManagerAddProduct())));
                primaryActions.getChildren().add(actionButton("New category", "Create a supplier category and product", () -> openRoute("inventory", () -> router().openManagerNewProductCategory())));
                primaryActions.getChildren().add(actionButton("Notifications", "Inspect low-stock signals", () -> openRoute("notifications", () -> router().openManagerNotifications())));
                primaryActions.getChildren().add(actionButton("Reports", "Open sales and cashier reports", () -> openRoute("reports", () -> router().openManagerReports())));
            }
            case ADMINISTRATOR -> {
                primaryActions.getChildren().add(actionButton("Add employee", "Create a cashier, manager, or administrator", () -> openRoute("employees", () -> router().openAdminAddEmployee())));
                primaryActions.getChildren().add(actionButton("Edit employee", "Update access or compensation", () -> openRoute("employees", () -> router().openAdminEditEmployee())));
                primaryActions.getChildren().add(actionButton("Delete employee", "Remove an employee record", () -> openRoute("employees", () -> router().openAdminDeleteEmployee())));
                primaryActions.getChildren().add(actionButton("Financial reports", "Review revenue, salaries, and expenses", () -> openRoute("financial reports", () -> router().openAdminFinancialReports())));
            }
        }

    }

    private void configurePreviewRoutes(Role role) {
        if (role != Role.CASHIER) {
            return;
        }

        previewTable.setOnMouseClicked(event -> {
            if (event.getClickCount() < 2) {
                return;
            }
            DashboardRow selected = previewTable.getSelectionModel().getSelectedItem();
            if (selected == null) {
                return;
            }
            String selectedItem = selected.itemProperty().get();
            if ("Create bill".equals(selectedItem)) {
                openRoute("bill editor", () -> router().openCashierBillEditor());
            } else if ("Bill history".equals(selectedItem)) {
                openRoute("bill history", () -> router().openCashierBillHistory());
            }
        });
    }

    private void openRoute(String routeName, Runnable action) {
        try {
            action.run();
        } catch (RuntimeException ex) {
            logRouteFailure(routeName, ex);
            setStatus("Unable to open " + routeName + ": " + rootMessage(ex), true);
        }
    }

    private Button actionButton(String title, String caption, Runnable action) {
        Button button = new Button(title + "\n" + caption);
        button.getStyleClass().add("action-card");
        button.setMaxWidth(Double.MAX_VALUE);
        button.setOnAction(event -> action.run());
        return button;
    }

    private void loadOperationalSnapshot(Role role) {
        try {
            List<Product> products = inventoryService.findAllProducts();
            long lowStock = products.stream().filter(inventoryService::isLowStock).count();
            int notifications = switch (role) {
                case CASHIER -> 0;
                case MANAGER, ADMINISTRATOR -> notificationService.findBySector(1).size()
                        + notificationService.findBySector(2).size()
                        + notificationService.findBySector(3).size();
            };

            statProducts.setText(String.valueOf(products.size()));
            statLowStock.setText(String.valueOf(lowStock));
            statNotifications.setText(String.valueOf(notifications));
        } catch (DatabaseException ex) {
            statProducts.setText("--");
            statLowStock.setText("--");
            statNotifications.setText("--");
        }
    }

    private void setStatus(String message, boolean error) {
        if (statusLabel == null) {
            return;
        }
        statusLabel.setText(message == null ? "" : message);
        statusLabel.getStyleClass().removeAll("status-error", "status-success");
        statusLabel.getStyleClass().add(error ? "status-error" : "status-success");
    }

    private String rootMessage(Throwable throwable) {
        Throwable cursor = throwable;
        while (cursor.getCause() != null) {
            cursor = cursor.getCause();
        }
        String message = cursor.getMessage();
        return message == null || message.isBlank() ? cursor.getClass().getSimpleName() : message;
    }

    private void logRouteFailure(String routeName, Throwable throwable) {
        System.err.println("[Model1] Dashboard route failed: " + routeName);
        System.err.println("[Model1] Cause tree:");
        int depth = 0;
        Throwable cursor = throwable;
        while (cursor != null) {
            System.err.println("  ".repeat(depth) + "- " + cursor.getClass().getName() + ": " + cursor.getMessage());
            cursor = cursor.getCause();
            depth++;
        }
        System.err.println("[Model1] Full stack trace:");
        throwable.printStackTrace(System.err);
    }

    private void playScanAnimation() {
        scanTimeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(scanLine.translateXProperty(), -260)),
                new KeyFrame(Duration.seconds(4.2), new KeyValue(scanLine.translateXProperty(), 460)));
        scanTimeline.setCycleCount(Animation.INDEFINITE);
        scanTimeline.play();
    }

    private Role activeRole() {
        return AppContext.getInstance()
                .getActiveRole()
                .orElse(Role.CASHIER);
    }

    private SceneRouter router() {
        return AppContext.getInstance()
                .getSceneRouter()
                .orElseThrow(() -> new IllegalStateException("SceneRouter has not been initialized."));
    }

    private String displayRole(Role role) {
        return switch (role) {
            case CASHIER -> "Cashier";
            case MANAGER -> "Manager";
            case ADMINISTRATOR -> "Administrator";
        };
    }

    public static final class DashboardRow {
        private final StringProperty item;
        private final StringProperty detail;
        private final StringProperty status;

        DashboardRow(String item, String detail, String status) {
            this.item = new SimpleStringProperty(item);
            this.detail = new SimpleStringProperty(detail);
            this.status = new SimpleStringProperty(status);
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
