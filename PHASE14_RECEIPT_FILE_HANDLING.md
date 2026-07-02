# Phase 14: Receipt And File Handling

## Scope

Phase 14 makes receipt files portable and recoverable.

## Implemented Changes

- Added `receipts.directory` to `app.properties`.
- Updated `ReceiptService` to use a configurable receipt directory.
- New bills store portable receipt paths such as `receipts/modern-...txt`.
- Existing legacy receipt names still resolve as a fallback when opening history records.
- Added receipt opening through `Desktop.open`.
- Added graceful missing-file errors.
- Added receipt regeneration from saved `bill_items`.
- Added bill history actions:
  - open receipt
  - regenerate receipt

## Acceptance Mapping

- Receipt paths work on another machine: new paths are relative to the project runtime directory.
- Missing receipt files do not break bill history: missing files show a status error instead of crashing the scene.
- Receipts can be regenerated from database data: bill history reloads `bill_items` and passes them to `ReceiptService.regenerateReceipt`.

## Verification

Run from `Model1 (1)/Model`:

```powershell
mvn test
```

Latest result: build success.
