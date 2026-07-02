package main.java.com.model1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import main.java.com.model1.exception.AuthenticationException;
import main.java.com.model1.exception.InsufficientStockException;
import main.java.com.model1.exception.ValidationException;
import main.java.com.model1.model.dto.CashierReport;
import main.java.com.model1.model.dto.LoginRequest;
import main.java.com.model1.model.dto.SalesReport;
import main.java.com.model1.model.entity.Bill;
import main.java.com.model1.model.entity.BillItem;
import main.java.com.model1.model.entity.Employee;
import main.java.com.model1.model.entity.Product;
import main.java.com.model1.model.enums.AccessLevel;
import main.java.com.model1.model.enums.Role;
import main.java.com.model1.service.AuthService;
import main.java.com.model1.service.BillingService;
import main.java.com.model1.service.EmployeeService;
import main.java.com.model1.service.InventoryService;
import main.java.com.model1.service.ReportService;

class MigrationPhase15Test {
    @TempDir
    Path tempDir;

    private Path databasePath;

    @BeforeEach
    void setUpDatabase() throws Exception {
        databasePath = tempDir.resolve("phase15.db");
        System.setProperty("model1.db.url", "jdbc:sqlite:" + databasePath);
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + databasePath);
                Statement statement = connection.createStatement()) {
            createLegacySchema(statement);
            seedData(statement);
        }
    }

    @AfterEach
    void clearDatabaseOverride() {
        System.clearProperty("model1.db.url");
    }

    @Test
    void loginSuccessAndFailureAreValidated() throws Exception {
        AuthService authService = new AuthService();

        assertTrue(authService.login(new LoginRequest("cashier1", "secret", Role.CASHIER)));
        assertEquals(1, intQuery("SELECT COUNT(*) FROM cashier_Login WHERE cashierName='cashier1'"));
        assertFalse(authService.login(new LoginRequest("cashier1", "wrong", Role.CASHIER)));
        assertThrows(AuthenticationException.class,
                () -> authService.authenticate(new LoginRequest("cashier1", "wrong", Role.CASHIER)));
    }

    @Test
    void billingTotalsStockValidationAndRollbackAreProtected() {
        BillingService billingService = new BillingService();
        List<BillItem> items = List.of(
                new BillItem("Tea", 2, new BigDecimal("2.50"), 1),
                new BillItem("Sugar", 1, new BigDecimal("1.25"), 1));

        assertEquals(new BigDecimal("6.25"), billingService.calculateTotal(items));
        assertThrows(InsufficientStockException.class,
                () -> billingService.prepareItem(new main.java.com.model1.model.dto.BillItemRequest("Sugar", 99, 1)));

        Bill bill = billingService.finishBill("cashier1", items, "receipts/test-bill.txt");
        assertEquals(new BigDecimal("6.25"), bill.total());
        assertEquals(8, intQuery("SELECT quantity FROM product WHERE productname='Tea' AND sector=1"));
        assertEquals(2, intQuery("SELECT COUNT(*) FROM bill_items WHERE billid=" + bill.id()));

        assertThrows(InsufficientStockException.class,
                () -> billingService.finishBill("cashier1",
                        List.of(new BillItem("Sugar", 50, new BigDecimal("1.25"), 1)),
                        "receipts/failed-bill.txt"));
        assertEquals(0, intQuery("SELECT COUNT(*) FROM bill WHERE filename='receipts/failed-bill.txt'"));
    }

    @Test
    void failedBillCreationRollsBackBillAndInventoryTogether() {
        BillingService billingService = new BillingService();

        assertThrows(InsufficientStockException.class,
                () -> billingService.finishBill("cashier1",
                        List.of(new BillItem("Sugar", 50, new BigDecimal("1.25"), 1)),
                        "receipts/rollback-bill.txt"));

        assertEquals(0, intQuery("SELECT COUNT(*) FROM bill WHERE filename='receipts/rollback-bill.txt'"));
        assertEquals(3, intQuery("SELECT quantity FROM product WHERE productname='Sugar' AND sector=1"));
    }

    @Test
    void refillRecordsExpenseAndResolvesNotification() {
        InventoryService inventoryService = new InventoryService();

        inventoryService.refillProduct("Sugar", 5, 1);

        assertEquals(8, intQuery("SELECT quantity FROM product WHERE productname='Sugar' AND sector=1"));
        assertEquals(5, intQuery("SELECT itemspurchased FROM expenses ORDER BY rowid DESC LIMIT 1"));
        assertEquals(0, intQuery("SELECT COUNT(*) FROM notifications WHERE notificationproduct='Sugar' AND notificationsector=1"));
    }

    @Test
    void reportCalculationsMatchRepositoryTotals() {
        ReportService reportService = new ReportService();
        BillingService billingService = new BillingService();
        billingService.finishBill("cashier1",
                List.of(new BillItem("Tea", 2, new BigDecimal("2.50"), 1)),
                "receipts/report-bill.txt");

        LocalDate today = LocalDate.now();
        SalesReport salesReport = reportService.salesReport(today, today);
        CashierReport cashierReport = reportService.cashierReport("cashier1", today, today);

        assertEquals(2, salesReport.totalItemsSold());
        assertTrue(salesReport.salesRevenue().compareTo(new BigDecimal("5.00")) >= 0);
        assertEquals(1, cashierReport.totalBills());
        assertEquals(2, cashierReport.totalItemsSold());
    }

    @Test
    void employeeValidationAndCrudUseRoleSpecificRules() {
        EmployeeService employeeService = new EmployeeService();
        LocalDate birthDate = LocalDate.of(1998, 1, 12);

        assertThrows(ValidationException.class,
                () -> employeeService.createEmployee(
                        Role.CASHIER,
                        "Invalid Access",
                        birthDate,
                        "555-3333",
                        "invalid@example.com",
                        new BigDecimal("1000"),
                        AccessLevel.MANAGER,
                        "invalid",
                        "pw",
                        1));

        Employee created = employeeService.createEmployee(
                Role.MANAGER,
                "New Manager",
                birthDate,
                "555-4444",
                "manager@example.com",
                new BigDecimal("2500"),
                AccessLevel.MANAGER,
                "newmanager",
                "pw",
                2);
        assertEquals(1, intQuery("SELECT COUNT(*) FROM manager WHERE musername='newmanager'"));

        Employee updated = employeeService.employee(
                Role.MANAGER,
                created.getId(),
                "New Manager",
                birthDate,
                "555-4444",
                "manager@example.com",
                new BigDecimal("2750"),
                AccessLevel.MANAGER,
                "newmanager",
                "pw",
                2);
        employeeService.update(updated);
        assertEquals("2750", stringQuery("SELECT msalary FROM manager WHERE musername='newmanager'"));

        employeeService.delete(Role.MANAGER, created.getId());
        assertEquals(0, intQuery("SELECT COUNT(*) FROM manager WHERE musername='newmanager'"));
    }

    private void createLegacySchema(Statement statement) throws SQLException {
        statement.execute("CREATE TABLE supplier(supplierid INTEGER PRIMARY KEY, suppliername TEXT, suppliercategory TEXT)");
        statement.execute("CREATE TABLE product(productname TEXT, quantity INTEGER, productprice NUMERIC, supplierid INTEGER, sector INTEGER)");
        statement.execute("CREATE TABLE expenses(expensedate TEXT, itemspurchased INTEGER, expense NUMERIC, expenseSector INTEGER)");
        statement.execute("CREATE TABLE notifications(notificationproduct TEXT, notificationsector INTEGER)");
        statement.execute("CREATE TABLE bill(billid INTEGER PRIMARY KEY, billdate TEXT, total NUMERIC, filename TEXT, cashier TEXT, totalitems INTEGER)");
        statement.execute("CREATE TABLE cashier_Login(cashierName TEXT, loginId INTEGER)");
        statement.execute("CREATE TABLE cashier(cid INTEGER PRIMARY KEY, cname TEXT, cbirthdate TEXT, cphonenumber TEXT, email TEXT, csalary NUMERIC, caccesslevel INTEGER, cusername TEXT, cpassword TEXT, sector INTEGER)");
        statement.execute("CREATE TABLE manager(mid INTEGER PRIMARY KEY, mname TEXT, mbirthdate TEXT, mphonenumber TEXT, memail TEXT, msalary NUMERIC, maccesslevel INTEGER, musername TEXT, mpassword TEXT, sector INTEGER)");
        statement.execute("CREATE TABLE administrator(aid INTEGER PRIMARY KEY, aname TEXT, abirthdate TEXT, aphonenumber TEXT, aemail TEXT, asalary NUMERIC, aaccesslevel INTEGER, ausername TEXT, apassword TEXT, sector INTEGER)");
    }

    private void seedData(Statement statement) throws SQLException {
        statement.execute("INSERT INTO supplier VALUES (1, 'Default Supplier', 'Grocery')");
        statement.execute("INSERT INTO product VALUES ('Tea', 10, 2.50, 1, 1)");
        statement.execute("INSERT INTO product VALUES ('Sugar', 3, 1.25, 1, 1)");
        statement.execute("INSERT INTO notifications VALUES ('Sugar', 1)");
        statement.execute("INSERT INTO cashier VALUES (1, 'Cashier One', '01121998', '555-1111', 'cashier@example.com', 1000, 1, 'cashier1', 'secret', 1)");
        statement.execute("INSERT INTO manager VALUES (1, 'Manager One', '01121990', '555-2222', 'manager1@example.com', 2000, 2, 'manager1', 'secret', 2)");
        statement.execute("INSERT INTO administrator VALUES (1, 'Admin One', '01121980', '555-9999', 'admin@example.com', 3000, 3, 'admin1', 'secret', 3)");
    }

    private int intQuery(String sql) {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + databasePath);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)) {
            return resultSet.next() ? resultSet.getInt(1) : 0;
        } catch (SQLException ex) {
            throw new AssertionError(ex);
        }
    }

    private String stringQuery(String sql) {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + databasePath);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)) {
            return resultSet.next() ? resultSet.getString(1) : "";
        } catch (SQLException ex) {
            throw new AssertionError(ex);
        }
    }
}
