# Phase 4 Repository Layer Status

Date completed: 2026-06-24

Phase 4 goal:

```text
Introduce repository classes that isolate SQL and map SQLite rows into Phase 3 domain objects.
```

## Completed

Added repository classes:

```text
AuthRepository
ProductRepository
SupplierRepository
BillRepository
ExpenseRepository
NotificationRepository
LoginHistoryRepository
EmployeeRepository
RepositoryMappers
```

Existing from Phase 2:

```text
DatabaseManager
```

## Repository Responsibilities

```text
AuthRepository
- Role-specific credential checks.

ProductRepository
- Find products.
- Find product names.
- Check available quantity.
- Increase/decrease stock through an existing connection.
- Save products through an existing connection.

SupplierRepository
- Find suppliers.
- Save suppliers through an existing connection.

BillRepository
- Resolve next bill id.
- Save bill summaries.
- Find bills by date or id.
- Count cashier bills by date range.

ExpenseRepository
- Find expenses.
- Save expenses.
- Read total purchased items.

NotificationRepository
- Find notifications by sector.
- Save notifications.
- Delete resolved notifications.

LoginHistoryRepository
- Find latest cashier login.
- Resolve next login id.
- Save cashier login activity.

EmployeeRepository
- Find employees by role.
- Find employee by role/name.
- Save employees.
- Delete employees by role/name.
- Update salary/access-level through explicit methods.
```

## SQL Safety Notes

The new `EmployeeRepository` does not accept arbitrary table names from UI strings.

Instead, it maps roles to known tables internally:

```text
CASHIER       -> cashier
MANAGER       -> manager
ADMINISTRATOR -> administrator
```

This is intentionally safer than the old dynamic SQL path in the legacy administrator edit/delete methods.

## Legacy Compatibility

Phase 4 is additive.

The existing JavaFX screens still use the legacy static model classes. Rewiring behavior into repositories is deferred to the service-layer phases so the migration stays incremental.

## Mapping Notes

`RepositoryMappers` contains compatibility helpers for the current database:

```text
Legacy dates -> LocalDate
SQLite numeric/text money values -> BigDecimal
LocalDate -> legacy MMddyyyy format
```

This keeps database quirks inside the repository layer instead of leaking them into domain objects.

## Verification

Verified:

- Repository package contains the expected repository classes.
- No JavaFX imports were found in `com.model1.repository`.
- Domain model package has no JavaFX or JDBC imports.
- SQL is now available through dedicated repository classes.

Not verified in this shell:

- Maven build.
- Java command-line compile.

Reason:

- `mvn` is not available on PATH.
- `java` and `javac` still return exit code `1` with no captured output from this shell.

Recommended VS Code check:

```text
Run Launch MainApp (Phase 1)
Confirm the existing login/database-backed flows still work
```

Because Phase 4 is additive, user-visible behavior should remain unchanged.

## Phase 4 Acceptance Criteria

Satisfied:

- SQL is isolated into repository classes for new migration code.
- Repositories use prepared statements.
- Repositories map rows into domain objects.
- Employee table access is selected by `Role`, not arbitrary UI strings.
- The repository layer has no JavaFX dependency.

