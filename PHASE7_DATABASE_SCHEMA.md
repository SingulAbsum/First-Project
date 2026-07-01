# Phase 7 Database Schema Status

Date completed: 2026-06-25

Phase 7 goal:

```text
Introduce a normalized v2 SQLite schema and migration path without breaking the current data.db runtime database.
```

## Completed

Added database migration artifacts:

```text
Model1 (1)\Model\database\schema_v2.sql
Model1 (1)\Model\database\migration_v1_to_v2.sql
Model1 (1)\Model\database\create_data_v2.py
Model1 (1)\Model\data_v2.db
```

The current app configuration still points to:

```text
database.path=data.db
```

That is intentional. Phase 7 prepares and verifies the normalized database; it does not switch the running app before repositories and UI flows are ready for the new schema.

## New Schema

Created normalized tables:

```text
roles
employees
users
suppliers
products
bills
bill_items
expenses
notifications
login_history
```

Key improvements:

```text
- Cashier, manager, and administrator rows are consolidated into employees.
- Login fields are isolated into users.
- Role rules are represented by roles.
- Usernames are unique per role, matching the current role-scoped login behavior.
- Products reference suppliers through a foreign key.
- Bills can now own detailed bill_items rows.
- Notifications can optionally reference products.
- Login history can optionally reference users.
```

## Migration Behavior

The SQL migration:

```text
- copies old suppliers into suppliers
- copies old products into products
- combines Cashier, Manager, and Administrator into employees
- creates linked users for migrated employees
- copies old bills into bills
- creates fallback bill_items from old bill summaries
- copies expenses into expenses
- copies notifications into notifications
- copies cashier_Login into login_history
```

The Python generation script additionally reads existing receipt text files and replaces fallback summary rows with parsed receipt line items when the receipt file is available.

If a legacy receipt file is missing or cannot be parsed, the bill still keeps a fallback `LEGACY_BILL_SUMMARY` item so no bill is left without item data.

## Verification

Verified migration counts for:

```text
roles: 3
employees: 6
users: 6
suppliers: 5
products: 11
bills: 21
expenses: 9
notifications: 1
login_history: 47
```

Verified that `bill_items` exists and contains:

```text
LEGACY_RECEIPT_FILE: 18
LEGACY_BILL_SUMMARY: 4
```

The 4 summary rows preserve bill totals where the legacy receipt file was not available or not parseable.

## Deferred

Still intentionally deferred to later phases:

```text
- Switch app.properties from data.db to data_v2.db.
- Update repositories to query the normalized v2 schema.
- Save real bill_items directly during checkout.
- Generate receipts only from database bill_items.
- Add repository tests against data_v2.db.
```

## Rebuild Command

From:

```text
Model1 (1)\Model
```

Run:

```text
python database\create_data_v2.py --replace
```

This rebuilds `data_v2.db` from the current `data.db`.
