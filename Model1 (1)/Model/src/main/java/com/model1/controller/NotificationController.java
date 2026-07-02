package main.java.com.model1.controller;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import main.java.com.model1.app.AppContext;
import main.java.com.model1.app.SceneRouter;
import main.java.com.model1.model.entity.Notification;
import main.java.com.model1.service.NotificationService;

public class NotificationController {
    @FXML
    private TableView<NotificationRow> notificationTable;
    @FXML
    private TableColumn<NotificationRow, String> productColumn;
    @FXML
    private TableColumn<NotificationRow, String> sectorColumn;
    @FXML
    private TableColumn<NotificationRow, String> statusColumn;
    @FXML
    private Label statusLabel;
    @FXML
    private Button resolveButton;

    private final NotificationService notificationService = new NotificationService();
    private final ObservableList<NotificationRow> rows = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        productColumn.setCellValueFactory(data -> data.getValue().productProperty());
        sectorColumn.setCellValueFactory(data -> data.getValue().sectorProperty());
        statusColumn.setCellValueFactory(data -> data.getValue().statusProperty());
        notificationTable.setItems(rows);
        notificationTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        notificationTable.setPlaceholder(emptyState("No low-stock notifications need attention."));
        configureActionStates();
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
            List<NotificationRow> loadedRows = new ArrayList<>();
            for (int sector = 1; sector <= 3; sector++) {
                for (Notification notification : notificationService.findBySector(sector)) {
                    loadedRows.add(new NotificationRow(notification));
                }
            }
            rows.setAll(loadedRows);
            setStatus(rows.size() + " low-stock notifications loaded.", false);
        } catch (RuntimeException ex) {
            logFailure("refresh notifications", ex);
            setStatus(ex.getMessage(), true);
        }
    }

    @FXML
    private void resolveSelected() {
        try {
            NotificationRow selected = notificationTable.getSelectionModel().getSelectedItem();
            if (selected == null) {
                setStatus("Select a notification first.", true);
                return;
            }
            notificationService.resolve(selected.notification());
            refreshData();
            setStatus(selected.notification().productName() + " notification resolved.", false);
        } catch (RuntimeException ex) {
            logFailure("resolve notification", ex);
            setStatus(ex.getMessage(), true);
        }
    }

    private void setStatus(String message, boolean error) {
        statusLabel.setText(message == null ? "" : message);
        statusLabel.getStyleClass().removeAll("status-error", "status-success");
        statusLabel.getStyleClass().add(error ? "status-error" : "status-success");
    }

    private void configureActionStates() {
        resolveButton.disableProperty().bind(notificationTable.getSelectionModel().selectedItemProperty().isNull());
    }

    private void logFailure(String action, Throwable throwable) {
        System.err.println("[Model1] Notification action failed: " + action);
        throwable.printStackTrace(System.err);
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

    public static final class NotificationRow {
        private final Notification notification;
        private final StringProperty product;
        private final StringProperty sector;
        private final StringProperty status;

        private NotificationRow(Notification notification) {
            this.notification = notification;
            this.product = new SimpleStringProperty(notification.productName());
            this.sector = new SimpleStringProperty(String.valueOf(notification.sector()));
            this.status = new SimpleStringProperty("Low stock");
        }

        private Notification notification() {
            return notification;
        }

        private StringProperty productProperty() {
            return product;
        }

        private StringProperty sectorProperty() {
            return sector;
        }

        private StringProperty statusProperty() {
            return status;
        }
    }
}
