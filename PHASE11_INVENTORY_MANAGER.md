# Phase 11: Inventory And Manager Module

## Scope

Phase 11 migrates the manager inventory workflow from placeholder module actions into a dedicated JavaFX controller and service-backed inventory screen.

## Implemented Changes

- Added `InventoryController` for the manager inventory scene.
- Replaced the manager inventory FXML binding from `ModuleController` to `InventoryController`.
- Product inventory is now listed in a `TableView`.
- Added product filtering by product name, supplier id, or sector.
- Added supplier selection through a `ComboBox`.
- Added product creation through `InventoryService.addProduct`.
- Added supplier/category creation through `InventoryService.addProductCategory`.
- Added refill workflow through `InventoryService.refillProduct`.
- Refills still record expenses and delete matching low-stock notifications through the service transaction.
- Added supplier auto-id support in `SupplierRepository`.
- Added supplier listing and new category convenience workflow to `InventoryService`.

## Acceptance Mapping

- Product add/refill flows use services: `InventoryController` calls `InventoryService`.
- Expenses are recorded correctly: product adds and refills use the existing expense creation paths.
- Notifications update after refill: `InventoryService.refillProduct` deletes the matching notification after stock increases.

## Verification

Run from `Model1 (1)/Model`:

```powershell
mvn test
```

Latest result: build success.
