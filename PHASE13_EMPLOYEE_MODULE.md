# Phase 13: Administrator And Employee Module

## Scope

Phase 13 migrates administrator employee management from generic placeholder actions into dedicated service-backed CRUD.

## Implemented Changes

- Added `EmployeeController`.
- Replaced administrator employee FXML with a dedicated employee management screen.
- Added role filtering for cashier, manager, and administrator employees.
- Added employee search by name, username, or email.
- Added employee creation through `EmployeeService`.
- Added employee edit through `EmployeeService.update`.
- Added employee delete through `EmployeeService.delete`.
- Extended `EmployeeRepository` with known-table next id, update, and delete-by-id methods.
- Expanded `EmployeeService` validation for role, access level, contact fields, credentials, salary, and sector.

## Acceptance Mapping

- Admin can manage all employee types: role filter and role selector support all `Role` values.
- Invalid edits are rejected before database write: validation runs in `EmployeeService`.
- Employee list refreshes after changes: add, edit, and delete all refresh the table.
- Avoid raw dynamic SQL based on UI strings: UI passes `Role`; repository maps it to known table metadata.

## Verification

Run from `Model1 (1)/Model`:

```powershell
mvn test
```

Latest result: build success.
