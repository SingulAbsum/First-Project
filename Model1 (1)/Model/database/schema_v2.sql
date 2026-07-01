PRAGMA foreign_keys = ON;

CREATE TABLE IF NOT EXISTS roles (
    id INTEGER PRIMARY KEY,
    code TEXT NOT NULL UNIQUE,
    display_name TEXT NOT NULL,
    minimum_access_level INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS employees (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    legacy_id INTEGER NOT NULL,
    role_id INTEGER NOT NULL,
    name TEXT NOT NULL,
    birth_date TEXT,
    phone_number TEXT,
    email TEXT,
    salary NUMERIC NOT NULL DEFAULT 0,
    access_level INTEGER NOT NULL DEFAULT 0,
    sector INTEGER NOT NULL DEFAULT 0,
    active INTEGER NOT NULL DEFAULT 1,
    created_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (role_id) REFERENCES roles(id),
    UNIQUE (role_id, legacy_id)
);

CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    employee_id INTEGER NOT NULL UNIQUE,
    role_id INTEGER NOT NULL,
    username TEXT NOT NULL,
    password TEXT NOT NULL,
    access_level INTEGER NOT NULL DEFAULT 0,
    active INTEGER NOT NULL DEFAULT 1,
    FOREIGN KEY (employee_id) REFERENCES employees(id),
    FOREIGN KEY (role_id) REFERENCES roles(id),
    UNIQUE (role_id, username)
);

CREATE TABLE IF NOT EXISTS suppliers (
    id INTEGER PRIMARY KEY,
    name TEXT NOT NULL,
    category TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS products (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    quantity INTEGER NOT NULL DEFAULT 0,
    price NUMERIC NOT NULL DEFAULT 0,
    supplier_id INTEGER,
    sector INTEGER NOT NULL DEFAULT 0,
    active INTEGER NOT NULL DEFAULT 1,
    FOREIGN KEY (supplier_id) REFERENCES suppliers(id),
    UNIQUE (name, sector)
);

CREATE TABLE IF NOT EXISTS bills (
    id INTEGER PRIMARY KEY,
    bill_date TEXT NOT NULL,
    total NUMERIC NOT NULL DEFAULT 0,
    receipt_file TEXT,
    cashier_employee_id INTEGER,
    cashier_name TEXT,
    total_items INTEGER NOT NULL DEFAULT 0,
    created_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (cashier_employee_id) REFERENCES employees(id)
);

CREATE TABLE IF NOT EXISTS bill_items (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    bill_id INTEGER NOT NULL,
    product_id INTEGER,
    product_name TEXT NOT NULL,
    quantity INTEGER NOT NULL,
    unit_price NUMERIC NOT NULL DEFAULT 0,
    line_total NUMERIC NOT NULL DEFAULT 0,
    sector INTEGER,
    source TEXT NOT NULL DEFAULT 'SERVICE',
    FOREIGN KEY (bill_id) REFERENCES bills(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id)
);

CREATE TABLE IF NOT EXISTS expenses (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    expense_date TEXT NOT NULL,
    items_purchased INTEGER NOT NULL DEFAULT 0,
    amount NUMERIC NOT NULL DEFAULT 0,
    sector INTEGER NOT NULL DEFAULT 0,
    created_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS notifications (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    product_id INTEGER,
    product_name TEXT NOT NULL,
    sector INTEGER NOT NULL DEFAULT 0,
    created_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
    resolved_at TEXT,
    FOREIGN KEY (product_id) REFERENCES products(id),
    UNIQUE (product_name, sector, resolved_at)
);

CREATE TABLE IF NOT EXISTS login_history (
    id INTEGER PRIMARY KEY,
    user_id INTEGER,
    cashier_name TEXT NOT NULL,
    login_at TEXT,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE INDEX IF NOT EXISTS idx_employees_role ON employees(role_id);
CREATE INDEX IF NOT EXISTS idx_users_role ON users(role_id);
CREATE INDEX IF NOT EXISTS idx_products_supplier ON products(supplier_id);
CREATE INDEX IF NOT EXISTS idx_products_name_sector ON products(name, sector);
CREATE INDEX IF NOT EXISTS idx_bills_date ON bills(bill_date);
CREATE INDEX IF NOT EXISTS idx_bills_cashier ON bills(cashier_employee_id, cashier_name);
CREATE INDEX IF NOT EXISTS idx_bill_items_bill ON bill_items(bill_id);
CREATE INDEX IF NOT EXISTS idx_expenses_date ON expenses(expense_date);
CREATE INDEX IF NOT EXISTS idx_notifications_sector ON notifications(sector);
CREATE INDEX IF NOT EXISTS idx_login_history_user ON login_history(user_id);
