# Modern JavaFX Architecture Migration Plan

This document describes a conceptual, step-by-step migration path for modernizing the current JavaFX + SQLite project while preserving the MVC direction.

The goal is not to rewrite everything at once. The safer path is to migrate one module at a time, keeping the current behavior understandable while gradually introducing cleaner architecture.

## Target Architecture

```text
src/main/java/com/yourname/storeapp
├── app
│   ├── MainApp.java
│   ├── AppContext.java
│   └── SceneRouter.java
│
├── model
│   ├── entity
│   │   ├── Employee.java
│   │   ├── Cashier.java
│   │   ├── Manager.java
│   │   ├── Administrator.java
│   │   ├── Product.java
│   │   ├── Supplier.java
│   │   ├── Bill.java
│   │   ├── BillItem.java
│   │   ├── Expense.java
│   │   └── Notification.java
│   │
│   ├── dto
│   │   ├── LoginRequest.java
│   │   ├── BillItemRequest.java
│   │   ├── CashierReport.java
│   │   └── FinancialReport.java
│   │
│   └── enums
│       ├── Role.java
│       └── AccessLevel.java
│
├── view
│   ├── fxml
│   │   ├── login.fxml
│   │   ├── cashier-dashboard.fxml
│   │   ├── manager-dashboard.fxml
│   │   ├── admin-dashboard.fxml
│   │   ├── bill-editor.fxml
│   │   ├── reports.fxml
│   │   └── inventory.fxml
│   │
│   └── css
│       └── app.css
│
├── controller
│   ├── LoginController.java
│   ├── CashierController.java
│   ├── ManagerController.java
│   ├── AdminController.java
│   ├── BillController.java
│   ├── InventoryController.java
│   └── ReportController.java
│
├── service
│   ├── AuthService.java
│   ├── BillingService.java
│   ├── InventoryService.java
│   ├── EmployeeService.java
│   ├── ReportService.java
│   ├── NotificationService.java
│   └── ReceiptService.java
│
├── repository
│   ├── DatabaseManager.java
│   ├── CashierRepository.java
│   ├── ManagerRepository.java
│   ├── AdministratorRepository.java
│   ├── ProductRepository.java
│   ├── BillRepository.java
│   ├── ExpenseRepository.java
│   └── SupplierRepository.java
│
├── util
│   ├── ValidationUtils.java
│   ├── DateUtils.java
│   ├── AlertUtils.java
│   └── ReceiptWriter.java
│
└── exception
    ├── AuthenticationException.java
    ├── ValidationException.java
    ├── DatabaseException.java
    ├── InsufficientStockException.java
    └── ReceiptGenerationException.java
```

## Phase 0: Stabilize The Current Project

Goal: understand and preserve existing behavior before changing the structure.

Requirements:

- Create a backup branch or zip copy.
- Confirm the current Eclipse setup runs.
- Document the existing credentials, database file, receipt files, and workflows.
- List all current screens and what each one does.

Suggested deliverable:

```text
CURRENT_BEHAVIOR.md
```

Acceptance criteria:

- Existing behavior is documented.
- Current database and receipt file behavior is understood.
- No modernization work has started yet.

## Phase 1: Build Project Foundation

Goal: introduce a modern project shell.

Requirements:

- Choose Maven or Gradle.
- Create the standard layout:

```text
src/main/java
src/main/resources
src/test/java
```

- Add dependencies:
  - JavaFX
  - SQLite JDBC
  - JUnit 5
  - SLF4J + Logback
- Create base packages:

```text
app
controller
model
service
repository
util
exception
```

Acceptance criteria:

- The project builds from the command line.
- JavaFX starts successfully.
- SQLite JDBC is resolved through the build tool.

## Phase 2: Database Access Layer

Goal: centralize SQLite connection handling.

Requirements:

- Create `DatabaseManager`.
- Store the database path in configuration.
- Add connection helper methods.
- Add transaction helper support.

Acceptance criteria:

- New code does not call `DriverManager.getConnection(...)` directly.
- Repository classes receive database access through `DatabaseManager`.

## Phase 3: Domain Model Layer

Goal: replace long chains of primitive values with meaningful Java objects.

Create entities:

```text
Employee
Cashier
Manager
Administrator
Product
Supplier
Bill
BillItem
Expense
Notification
LoginHistory
```

Create enums:

```text
Role
AccessLevel
```

Requirements:

- Use `LocalDate` for dates.
- Use `BigDecimal` for money.
- Use domain names like `name`, `salary`, and `accessLevel`, not table prefixes like `cName` or `mSalary`.
- Keep database mapping inside repositories.

Acceptance criteria:

- Business logic can work with `Product`, `Bill`, `Employee`, and related objects.
- Controllers do not need to pass ten raw strings into one method.

## Phase 4: Repository Layer

Goal: isolate SQL into dedicated classes.

Suggested repositories:

```text
AuthRepository
EmployeeRepository
ProductRepository
SupplierRepository
BillRepository
ExpenseRepository
NotificationRepository
LoginHistoryRepository
```

Suggested implementation order:

1. `AuthRepository`
2. `ProductRepository`
3. `BillRepository`
4. `ExpenseRepository`
5. `NotificationRepository`
6. `EmployeeRepository`
7. `SupplierRepository`

Requirements:

- Every repository method should perform one clear database job.
- Use prepared statements.
- Avoid dynamic SQL from UI strings.
- Convert `ResultSet` rows into entity objects.

Acceptance criteria:

- New SQL lives only in repository classes.
- Services and controllers no longer need to know SQL details.

## Phase 5: Service Layer

Goal: move business rules out of JavaFX UI code.

Suggested services:

```text
AuthService
BillingService
InventoryService
EmployeeService
ReportService
NotificationService
ReceiptService
```

Suggested implementation order:

1. `AuthService`
2. `InventoryService`
3. `BillingService`
4. `ReceiptService`
5. `ReportService`
6. `NotificationService`
7. `EmployeeService`

Responsibilities:

```text
AuthService
- login
- validate role access
- record cashier login history

BillingService
- add item to current bill
- validate stock
- calculate totals
- finish bill
- update inventory
- save bill and bill items

InventoryService
- add product
- add product category
- refill product
- check low stock

ReportService
- cashier report
- general sales report
- admin financial report

EmployeeService
- create employee
- update employee
- delete employee
```

Acceptance criteria:

- Business logic is testable without JavaFX.
- Services accept domain objects or DTOs.
- Services throw meaningful exceptions instead of showing alerts directly.

## Phase 6: Validation And Exceptions

Goal: make invalid input and errors predictable.

Create utilities:

```text
ValidationUtils
MoneyParser
DateParser
```

Create exceptions:

```text
ValidationException
AuthenticationException
InsufficientStockException
DatabaseException
ReceiptGenerationException
```

Requirements:

- Validate empty fields before service calls.
- Validate numeric input safely.
- Validate dates with one consistent format.
- Convert SQL errors into app-level exceptions.

Acceptance criteria:

- Controllers do not blindly call `Integer.parseInt(...)`.
- UI alerts show meaningful messages based on exception type.

## Phase 7: Normalize The Database Schema

Goal: improve the schema without immediately breaking old data.

Suggested new schema:

```text
users
employees
roles
products
suppliers
bills
bill_items
expenses
notifications
login_history
```

Migration path:

1. Keep the current `data.db`.
2. Create `data_v2.db`.
3. Create migration scripts.
4. Copy current data into new tables.
5. Verify old and new report totals.
6. Switch app config to `data_v2.db`.

Important improvement:

- Add `bill_items`, so every sold item is stored in SQLite.
- Receipt files should be generated from database data, not act as the only detailed bill record.

Suggested deliverables:

```text
schema_v2.sql
migration_v1_to_v2.sql
data_v2.db
```

Acceptance criteria:

- Existing products, employees, suppliers, expenses, and bills migrate.
- Receipt files can be regenerated from database data.
- Reports no longer depend on text files.

## Phase 8: FXML And CSS UI Migration

Goal: modernize JavaFX screens gradually.

Suggested screen order:

1. Login screen
2. Role dashboard shell
3. Cashier bill editor
4. Cashier bill history
5. Manager inventory screen
6. Manager notifications screen
7. Manager reports screen
8. Admin employee management screen
9. Admin financial reports screen

Suggested resources:

```text
resources/view/login.fxml
resources/view/dashboard.fxml
resources/view/cashier/bill-editor.fxml
resources/view/manager/inventory.fxml
resources/view/admin/employees.fxml
resources/styles/app.css
```

Requirements:

- Move styling into CSS.
- Use `PasswordField` for passwords.
- Use `TableView` for products, bills, employees, and reports.
- Use sidebar navigation instead of scattered `MenuButton` navigation.
- Use modal dialogs for add/edit forms.

Acceptance criteria:

- Controllers are smaller.
- Screens are navigated through `SceneRouter`.
- Styling is centralized.

## Phase 9: Authentication Module Migration

Goal: fully migrate login first.

Implementation steps:

1. Create `LoginController`.
2. Create `AuthService`.
3. Create `AuthRepository`.
4. Replace old login button handlers.
5. Route users to dashboards by `Role`.
6. Store the active session in `AppContext`.

Requirements:

- Use `Role` enum.
- Use `PasswordField`.
- Record cashier login history.
- Display clean login errors.

Acceptance criteria:

- Cashier, manager, and administrator can log in.
- Invalid credentials show a clear error.
- Correct dashboard opens based on role.

## Phase 10: Cashier And Billing Module Migration

Goal: migrate the core point-of-sale workflow.

Implementation steps:

1. Create `BillController`.
2. Create `BillingService`.
3. Create `InventoryService`.
4. Create `BillRepository`.
5. Create `ProductRepository`.
6. Create `ReceiptService`.
7. Introduce `BillItem`.
8. Store bill items in the database.
9. Generate receipts from saved bill data.

Requirements:

- Product search/autocomplete.
- Quantity validation.
- Sector validation.
- Stock validation.
- Running total.
- Bill completion should be transactional:
  - save bill
  - save bill items
  - decrease stock
  - create notifications if needed
  - generate receipt

Acceptance criteria:

- A bill cannot complete if stock is insufficient.
- Inventory and bill records update together.
- Failed bill creation rolls back cleanly.
- Receipt generation is separate from database save.

## Phase 11: Inventory And Manager Module Migration

Goal: migrate manager product and stock workflows.

Implementation steps:

1. Build the inventory screen.
2. Add product creation.
3. Add supplier selection.
4. Add product category creation.
5. Add refill workflow.
6. Connect low-stock notifications.

Requirements:

- Products should be listed in a `TableView`.
- Suppliers should be selected where possible.
- Refills should create expenses.
- Notifications should be resolved when stock is refilled.

Acceptance criteria:

- Product add/refill flows use services.
- Expenses are recorded correctly.
- Notifications update after refill.

## Phase 12: Reporting Module Migration

Goal: centralize report calculations.

Implementation steps:

1. Create `ReportService`.
2. Create report DTOs:
   - `CashierReport`
   - `SalesReport`
   - `FinancialReport`
3. Move report SQL into repositories.
4. Replace old static report methods.

Reports:

```text
Cashier report
- bills generated
- items sold
- revenue by date range

Manager general report
- items sold
- items purchased
- revenue
- expenses
- balance

Admin financial report
- revenue
- expenses
- salaries
- balance
```

Requirements:

- Use `LocalDate` ranges.
- Use `BigDecimal`.
- Keep calculations consistent.

Acceptance criteria:

- Reports match existing database totals.
- Date ranges work predictably.
- Reports do not depend on UI code.

## Phase 13: Administrator And Employee Module Migration

Goal: migrate employee CRUD cleanly.

Implementation steps:

1. Create employee management screen.
2. Create `EmployeeService`.
3. Create `EmployeeRepository`.
4. Replace dynamic table/field update logic.
5. Use role-specific validation.

Requirements:

- Add employee.
- Edit employee.
- Delete employee.
- Filter employees by role.
- Avoid raw dynamic SQL based on UI strings.

Acceptance criteria:

- Admin can manage all employee types.
- Invalid edits are rejected before database write.
- Employee list refreshes after changes.

## Phase 14: Receipt And File Handling

Goal: make receipts portable and reliable.

Requirements:

- Create a configurable receipts directory.
- Do not hardcode absolute paths.
- Save receipt path in the database.
- Open receipts through `Desktop.open`.
- Handle missing files gracefully.
- Allow regenerating receipts from `bill_items`.

Suggested classes:

```text
ReceiptService
ReceiptWriter
```

Acceptance criteria:

- Receipt paths work on another machine.
- Missing receipt files do not break bill history.
- Receipts can be regenerated from database data.

## Phase 15: Testing

Goal: protect migrated behavior from regressions.

Add tests for:

- Login success and failure.
- Product stock validation.
- Bill total calculation.
- Bill completion transaction.
- Refill expense creation.
- Report calculations.
- Employee validation.

Suggested test structure:

```text
src/test/java/.../service
src/test/java/.../repository
```

Suggested tools:

```text
JUnit 5
Mockito
Temporary SQLite test database
```

Acceptance criteria:

- Core services have unit tests.
- Repository tests use a test database.
- Billing transaction rollback is tested.

## Phase 16: Cleanup Legacy Code

Goal: remove old structure only after replacement is complete.

Remove or retire:

- Static database helper methods.
- Empty placeholder classes.
- Duplicate navigation wrapper classes.
- Hardcoded paths.
- Old compiled `bin` output from source control or distribution package.
- Generic reused alerts with misleading messages.

Acceptance criteria:

- No active code depends on old static model classes.
- Project builds cleanly.
- Folder structure matches the target architecture.

## Recommended Implementation Order

```text
1. Build tool and project structure
2. DatabaseManager
3. Entities
4. Auth module
5. Cashier billing module
6. Receipt handling
7. Inventory module
8. Notifications
9. Reports
10. Administrator employee management
11. Database schema v2
12. UI polish
13. Tests and cleanup
```

Authentication and billing should come early because they are the heart of the application. Once those are clean, the manager and administrator modules become much easier to migrate around them.

## Definition Of Done

The migration is complete when:

```text
The app launches from Maven or Gradle.
Users log in by role.
Cashiers create bills through the new billing service.
Bills store line items in SQLite.
Receipts are generated from saved bill data.
Managers handle inventory, notifications, and reports.
Admins manage employees and financial reports.
No business logic lives inside JavaFX view classes.
No SQL lives outside repositories.
No absolute machine-specific paths are required.
Core services have tests.
```

