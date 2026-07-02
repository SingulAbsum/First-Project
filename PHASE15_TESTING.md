# Phase 15: Testing

## Scope

Phase 15 adds regression coverage for the migrated service behavior using isolated temporary SQLite databases.

## Added Tests

- Login success and failure.
- Cashier login history recording.
- Product stock validation.
- Bill total calculation.
- Successful bill persistence with bill items and inventory update.
- Failed bill completion rollback.
- Refill expense creation.
- Low-stock notification cleanup after refill.
- Sales and cashier report calculations.
- Employee role/access validation.
- Employee create, update, and delete.

## Verification

Run from `Model1 (1)/Model`:

```powershell
mvn test
```

The tests use `model1.db.url` with a temporary SQLite file and do not mutate the project `data.db`.
