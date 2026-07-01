# Phase 12: Reporting Module

## Scope

Phase 12 centralizes reporting behind `ReportService` and replaces generic report screens with dedicated service-backed report views.

## Implemented Changes

- Added `ReportController` for manager and administrator report scenes.
- Replaced manager reports FXML with a `ReportController` view.
- Replaced administrator financial reports FXML with a `ReportController` view.
- Kept calculations in `ReportService`.
- Kept report SQL in repositories:
  - `BillRepository`
  - `ExpenseRepository`
  - `EmployeeRepository`
- Used existing report DTOs:
  - `CashierReport`
  - `SalesReport`
  - `FinancialReport`
- Added predictable default date range: first day of the current month through today.
- Report screens accept `yyyy-MM-dd` and legacy `MMddyyyy` date inputs.

## Report Coverage

Manager reports:

- Cashier report:
  - bills generated
  - items sold
  - revenue by date range
- Sales report:
  - items sold
  - items purchased
  - revenue
  - purchase expense
  - balance

Administrator financial report:

- revenue
- purchase expense
- salary expense
- total expense
- balance

## Acceptance Mapping

- Reports match repository totals: all report values come from repository aggregate methods through `ReportService`.
- Date ranges work predictably: report controller defaults dates and parses both ISO and legacy formats.
- Reports do not depend on UI code: calculations remain in `ReportService`; controller only renders DTO values.

## Verification

Run from `Model1 (1)/Model`:

```powershell
mvn test
```

Latest result: build success.
