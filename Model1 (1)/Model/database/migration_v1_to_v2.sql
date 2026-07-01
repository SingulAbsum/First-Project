-- Run this script against an empty v2 database after executing schema_v2.sql.
-- The current v1 database must be attached as schema name `old`.
--
-- Example:
--   ATTACH DATABASE '../data.db' AS old;
--   .read schema_v2.sql
--   .read migration_v1_to_v2.sql

PRAGMA foreign_keys = OFF;

BEGIN;

INSERT OR IGNORE INTO roles (id, code, display_name, minimum_access_level)
VALUES
    (1, 'CASHIER', 'Cashier', 1),
    (2, 'MANAGER', 'Manager', 2),
    (3, 'ADMINISTRATOR', 'Administrator', 3);

INSERT OR IGNORE INTO suppliers (id, name, category)
SELECT supplierId, supplierName, supplierCategory
FROM old.Supplier;

INSERT OR IGNORE INTO employees (
    legacy_id,
    role_id,
    name,
    birth_date,
    phone_number,
    email,
    salary,
    access_level,
    sector
)
SELECT
    cId,
    1,
    cName,
    substr(printf('%08d', CAST(cBirthDate AS INTEGER)), 5, 4)
        || '-' || substr(printf('%08d', CAST(cBirthDate AS INTEGER)), 1, 2)
        || '-' || substr(printf('%08d', CAST(cBirthDate AS INTEGER)), 3, 2),
    CAST(cPhoneNumber AS TEXT),
    email,
    cSalary,
    cAccessLevel,
    sector
FROM old.Cashier;

INSERT OR IGNORE INTO employees (
    legacy_id,
    role_id,
    name,
    birth_date,
    phone_number,
    email,
    salary,
    access_level,
    sector
)
SELECT
    mId,
    2,
    mName,
    substr(printf('%08d', CAST(mBirthDate AS INTEGER)), 5, 4)
        || '-' || substr(printf('%08d', CAST(mBirthDate AS INTEGER)), 1, 2)
        || '-' || substr(printf('%08d', CAST(mBirthDate AS INTEGER)), 3, 2),
    CAST(mPhoneNumber AS TEXT),
    mEmail,
    mSalary,
    mAccessLevel,
    sector
FROM old.Manager;

INSERT OR IGNORE INTO employees (
    legacy_id,
    role_id,
    name,
    birth_date,
    phone_number,
    email,
    salary,
    access_level,
    sector
)
SELECT
    aId,
    3,
    aName,
    substr(printf('%08d', CAST(aBirthDate AS INTEGER)), 5, 4)
        || '-' || substr(printf('%08d', CAST(aBirthDate AS INTEGER)), 1, 2)
        || '-' || substr(printf('%08d', CAST(aBirthDate AS INTEGER)), 3, 2),
    CAST(aPhoneNumber AS TEXT),
    aEmail,
    aSalary,
    aAccessLevel,
    sector
FROM old.Administrator;

INSERT OR IGNORE INTO users (
    employee_id,
    role_id,
    username,
    password,
    access_level,
    active
)
SELECT
    e.id,
    1,
    c.cUsername,
    c.cPassword,
    c.cAccessLevel,
    CASE WHEN c.cAccessLevel > 0 THEN 1 ELSE 0 END
FROM old.Cashier c
JOIN employees e ON e.role_id = 1 AND e.legacy_id = c.cId;

INSERT OR IGNORE INTO users (
    employee_id,
    role_id,
    username,
    password,
    access_level,
    active
)
SELECT
    e.id,
    2,
    m.mUsername,
    m.mPassword,
    m.mAccessLevel,
    CASE WHEN m.mAccessLevel > 1 THEN 1 ELSE 0 END
FROM old.Manager m
JOIN employees e ON e.role_id = 2 AND e.legacy_id = m.mId;

INSERT OR IGNORE INTO users (
    employee_id,
    role_id,
    username,
    password,
    access_level,
    active
)
SELECT
    e.id,
    3,
    a.aUsername,
    a.aPassword,
    a.aAccessLevel,
    CASE WHEN a.aAccessLevel > 2 THEN 1 ELSE 0 END
FROM old.Administrator a
JOIN employees e ON e.role_id = 3 AND e.legacy_id = a.aId;

INSERT OR IGNORE INTO products (
    name,
    quantity,
    price,
    supplier_id,
    sector
)
SELECT
    productName,
    quantity,
    productPrice,
    supplierId,
    sector
FROM old.Product;

INSERT OR IGNORE INTO bills (
    id,
    bill_date,
    total,
    receipt_file,
    cashier_employee_id,
    cashier_name,
    total_items
)
SELECT
    b.billId,
    substr(printf('%08d', CAST(b.billDate AS INTEGER)), 5, 4)
        || '-' || substr(printf('%08d', CAST(b.billDate AS INTEGER)), 1, 2)
        || '-' || substr(printf('%08d', CAST(b.billDate AS INTEGER)), 3, 2),
    b.total,
    b.fileName,
    e.id,
    b.cashier,
    COALESCE(b.totalItems, 0)
FROM old.Bill b
LEFT JOIN employees e ON e.role_id = 1 AND e.name = b.cashier;

INSERT INTO bill_items (
    bill_id,
    product_name,
    quantity,
    unit_price,
    line_total,
    source
)
SELECT
    billId,
    'Legacy receipt summary',
    CASE WHEN COALESCE(totalItems, 0) > 0 THEN totalItems ELSE 1 END,
    CASE WHEN COALESCE(totalItems, 0) > 0 THEN CAST(total AS REAL) / totalItems ELSE CAST(total AS REAL) END,
    total,
    'LEGACY_BILL_SUMMARY'
FROM old.Bill
WHERE total IS NOT NULL;

INSERT INTO expenses (
    expense_date,
    items_purchased,
    amount,
    sector
)
SELECT
    substr(printf('%08d', CAST(expenseDate AS INTEGER)), 5, 4)
        || '-' || substr(printf('%08d', CAST(expenseDate AS INTEGER)), 1, 2)
        || '-' || substr(printf('%08d', CAST(expenseDate AS INTEGER)), 3, 2),
    itemsPurchased,
    expense,
    expenseSector
FROM old.expenses;

INSERT OR IGNORE INTO notifications (
    product_id,
    product_name,
    sector
)
SELECT
    p.id,
    n.notificationProduct,
    n.notificationSector
FROM old.Notifications n
LEFT JOIN products p ON p.name = n.notificationProduct AND p.sector = n.notificationSector;

INSERT OR IGNORE INTO login_history (
    id,
    user_id,
    cashier_name
)
SELECT
    l.loginId,
    u.id,
    l.cashierName
FROM old.cashier_Login l
LEFT JOIN employees e ON e.role_id = 1 AND e.name = l.cashierName
LEFT JOIN users u ON u.employee_id = e.id;

COMMIT;

PRAGMA foreign_keys = ON;
