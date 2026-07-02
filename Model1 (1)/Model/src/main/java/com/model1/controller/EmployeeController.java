package main.java.com.model1.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Locale;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import main.java.com.model1.app.AppContext;
import main.java.com.model1.app.SceneRouter;
import main.java.com.model1.model.entity.Employee;
import main.java.com.model1.model.enums.AccessLevel;
import main.java.com.model1.model.enums.Role;
import main.java.com.model1.service.EmployeeService;
import main.java.com.model1.util.DateParser;
import main.java.com.model1.util.ValidationUtils;

public class EmployeeController {
    @FXML
    private ComboBox<Role> roleFilterComboBox;
    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<Role> roleComboBox;
    @FXML
    private ComboBox<AccessLevel> accessComboBox;
    @FXML
    private TextField nameField;
    @FXML
    private DatePicker birthDatePicker;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField salaryField;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private ComboBox<Integer> sectorComboBox;
    @FXML
    private TableView<EmployeeRow> employeeTable;
    @FXML
    private TableColumn<EmployeeRow, String> idColumn;
    @FXML
    private TableColumn<EmployeeRow, String> nameColumn;
    @FXML
    private TableColumn<EmployeeRow, String> roleColumn;
    @FXML
    private TableColumn<EmployeeRow, String> contactColumn;
    @FXML
    private TableColumn<EmployeeRow, String> salaryColumn;
    @FXML
    private TableColumn<EmployeeRow, String> accessColumn;
    @FXML
    private TableColumn<EmployeeRow, String> sectorColumn;
    @FXML
    private Label statusLabel;
    @FXML
    private Button editEmployeeButton;
    @FXML
    private Button deleteEmployeeButton;

    private final EmployeeService employeeService = new EmployeeService();
    private final ObservableList<EmployeeRow> rows = FXCollections.observableArrayList();
    private Employee selectedEmployee;

    @FXML
    private void initialize() {
        configureCombos();
        configureTable();
        employeeTable.setItems(rows);
        configureActionStates();
        searchField.textProperty().addListener((observable, previous, current) -> refreshData());
        roleFilterComboBox.valueProperty().addListener((observable, previous, current) -> refreshData());
        roleComboBox.valueProperty().addListener((observable, previous, current) -> syncAccessLevel());
        employeeTable.getSelectionModel().selectedItemProperty().addListener((observable, previous, selected) -> populateSelection(selected));
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
            Role role = roleFilterComboBox.getValue();
            String filter = searchField == null ? "" : searchField.getText().trim().toLowerCase(Locale.ROOT);
            rows.setAll(employeeService.findByRole(role).stream()
                    .filter(employee -> filter.isBlank()
                            || employee.getName().toLowerCase(Locale.ROOT).contains(filter)
                            || employee.getUsername().toLowerCase(Locale.ROOT).contains(filter)
                            || employee.getEmail().toLowerCase(Locale.ROOT).contains(filter))
                    .map(EmployeeRow::new)
                    .toList());
            setStatus(rows.size() + " " + displayRole(role) + " employees loaded.", false);
        } catch (RuntimeException ex) {
            logFailure("refresh employees", ex);
            setStatus(ex.getMessage(), true);
        }
    }

    @FXML
    private void addEmployee() {
        try {
            Role role = selectedRole();
            Employee employee = employeeService.createEmployee(
                    role,
                    text(nameField, "Employee name"),
                    date(birthDatePicker, "Birth date"),
                    text(phoneField, "Phone number"),
                    text(emailField, "Email"),
                    moneyValue(salaryField, "Salary"),
                    selectedAccessLevel(),
                    text(usernameField, "Username"),
                    text(passwordField, "Password"),
                    selectedSector(sectorComboBox, "Sector"));
            roleFilterComboBox.setValue(employee.getRole());
            clearForm();
            refreshData();
            setStatus(employee.getName() + " added.", false);
        } catch (RuntimeException ex) {
            logFailure("add employee", ex);
            setStatus(ex.getMessage(), true);
        }
    }

    @FXML
    private void editEmployee() {
        try {
            ValidationUtils.requireNonNull(selectedEmployee, "Selected employee");
            Role role = selectedEmployee.getRole();
            if (roleComboBox.getValue() != role) {
                throw new IllegalArgumentException("Role cannot be changed while editing. Delete and recreate the employee under the new role.");
            }
            Employee employee = employeeService.employee(
                    role,
                    selectedEmployee.getId(),
                    text(nameField, "Employee name"),
                    date(birthDatePicker, "Birth date"),
                    text(phoneField, "Phone number"),
                    text(emailField, "Email"),
                    moneyValue(salaryField, "Salary"),
                    selectedAccessLevel(),
                    text(usernameField, "Username"),
                    text(passwordField, "Password"),
                    selectedSector(sectorComboBox, "Sector"));
            employeeService.update(employee);
            clearForm();
            refreshData();
            setStatus(employee.getName() + " updated.", false);
        } catch (RuntimeException ex) {
            logFailure("edit employee", ex);
            setStatus(ex.getMessage(), true);
        }
    }

    @FXML
    private void deleteEmployee() {
        try {
            ValidationUtils.requireNonNull(selectedEmployee, "Selected employee");
            employeeService.delete(selectedEmployee.getRole(), selectedEmployee.getId());
            String name = selectedEmployee.getName();
            clearForm();
            refreshData();
            setStatus(name + " deleted.", false);
        } catch (RuntimeException ex) {
            logFailure("delete employee", ex);
            setStatus(ex.getMessage(), true);
        }
    }

    @FXML
    private void clearForm() {
        selectedEmployee = null;
        employeeTable.getSelectionModel().clearSelection();
        roleComboBox.setValue(roleFilterComboBox.getValue());
        syncAccessLevel();
        nameField.clear();
        birthDatePicker.setValue(null);
        phoneField.clear();
        emailField.clear();
        salaryField.clear();
        usernameField.clear();
        passwordField.clear();
        sectorComboBox.getSelectionModel().selectFirst();
    }

    private void configureCombos() {
        roleFilterComboBox.setItems(FXCollections.observableArrayList(Role.values()));
        roleComboBox.setItems(FXCollections.observableArrayList(Role.values()));
        accessComboBox.setItems(FXCollections.observableArrayList(AccessLevel.values()));
        sectorComboBox.setItems(FXCollections.observableArrayList(1, 2, 3));
        roleFilterComboBox.setValue(Role.CASHIER);
        roleComboBox.setValue(Role.CASHIER);
        accessComboBox.setValue(AccessLevel.CASHIER);
        sectorComboBox.getSelectionModel().selectFirst();
    }

    private void configureTable() {
        idColumn.setCellValueFactory(data -> data.getValue().idProperty());
        nameColumn.setCellValueFactory(data -> data.getValue().nameProperty());
        roleColumn.setCellValueFactory(data -> data.getValue().roleProperty());
        contactColumn.setCellValueFactory(data -> data.getValue().contactProperty());
        salaryColumn.setCellValueFactory(data -> data.getValue().salaryProperty());
        accessColumn.setCellValueFactory(data -> data.getValue().accessProperty());
        sectorColumn.setCellValueFactory(data -> data.getValue().sectorProperty());
        employeeTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        employeeTable.setPlaceholder(emptyState("No employees match this role and filter."));
    }

    private void configureActionStates() {
        editEmployeeButton.disableProperty().bind(employeeTable.getSelectionModel().selectedItemProperty().isNull());
        deleteEmployeeButton.disableProperty().bind(employeeTable.getSelectionModel().selectedItemProperty().isNull());
    }

    private void populateSelection(EmployeeRow selected) {
        if (selected == null) {
            return;
        }
        selectedEmployee = selected.employee();
        roleComboBox.setValue(selectedEmployee.getRole());
        accessComboBox.setValue(selectedEmployee.getAccessLevel());
        nameField.setText(selectedEmployee.getName());
        birthDatePicker.setValue(selectedEmployee.getBirthDate());
        phoneField.setText(selectedEmployee.getPhoneNumber());
        emailField.setText(selectedEmployee.getEmail());
        salaryField.setText(selectedEmployee.getSalary().setScale(2, RoundingMode.HALF_UP).toPlainString());
        usernameField.setText(selectedEmployee.getUsername());
        passwordField.setText(selectedEmployee.getPassword());
        sectorComboBox.setValue(selectedEmployee.getSector());
    }

    private void syncAccessLevel() {
        Role role = roleComboBox.getValue();
        if (role != null) {
            accessComboBox.setValue(employeeService.expectedAccessLevel(role));
        }
    }

    private Role selectedRole() {
        return ValidationUtils.requireNonNull(roleComboBox.getValue(), "Role");
    }

    private AccessLevel selectedAccessLevel() {
        return ValidationUtils.requireNonNull(accessComboBox.getValue(), "Access level");
    }

    private String text(TextField field, String name) {
        return ValidationUtils.requireNonBlank(field == null ? "" : field.getText(), name);
    }

    private LocalDate date(DatePicker picker, String name) {
        if (picker != null && picker.getValue() != null) {
            return picker.getValue();
        }
        String typedValue = picker == null ? "" : picker.getEditor().getText();
        return DateParser.parseIsoOrLegacy(typedValue, name);
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

    private int selectedSector(ComboBox<Integer> comboBox, String name) {
        Integer value = comboBox == null ? null : comboBox.getValue();
        ValidationUtils.requireNonNull(value, name);
        ValidationUtils.requirePositive(value, name);
        return value;
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

    private String money(BigDecimal value) {
        return "$" + value.setScale(2, RoundingMode.HALF_UP);
    }

    private String displayRole(Role role) {
        return switch (role) {
            case CASHIER -> "cashier";
            case MANAGER -> "manager";
            case ADMINISTRATOR -> "administrator";
        };
    }

    private void setStatus(String message, boolean error) {
        statusLabel.setText(message == null ? "" : message);
        statusLabel.getStyleClass().removeAll("status-error", "status-success");
        statusLabel.getStyleClass().add(error ? "status-error" : "status-success");
    }

    private void logFailure(String action, Throwable throwable) {
        System.err.println("[Model1] Employee action failed: " + action);
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

    public final class EmployeeRow {
        private final Employee employee;
        private final StringProperty id;
        private final StringProperty name;
        private final StringProperty role;
        private final StringProperty contact;
        private final StringProperty salary;
        private final StringProperty access;
        private final StringProperty sector;

        private EmployeeRow(Employee employee) {
            this.employee = employee;
            this.id = new SimpleStringProperty(String.valueOf(employee.getId()));
            this.name = new SimpleStringProperty(employee.getName());
            this.role = new SimpleStringProperty(employee.getRole().name());
            this.contact = new SimpleStringProperty(employee.getEmail() + " / " + employee.getPhoneNumber());
            this.salary = new SimpleStringProperty(money(employee.getSalary()));
            this.access = new SimpleStringProperty(employee.getAccessLevel().name());
            this.sector = new SimpleStringProperty(String.valueOf(employee.getSector()));
        }

        private Employee employee() {
            return employee;
        }

        private StringProperty idProperty() {
            return id;
        }

        private StringProperty nameProperty() {
            return name;
        }

        private StringProperty roleProperty() {
            return role;
        }

        private StringProperty contactProperty() {
            return contact;
        }

        private StringProperty salaryProperty() {
            return salary;
        }

        private StringProperty accessProperty() {
            return access;
        }

        private StringProperty sectorProperty() {
            return sector;
        }
    }
}
