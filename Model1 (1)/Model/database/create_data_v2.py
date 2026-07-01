from __future__ import annotations

import argparse
import re
import sqlite3
from pathlib import Path


RECEIPT_ITEM = re.compile(
    r"^(?P<name>.+?)\s+(?P<quantity>\d+)x(?P<unit_price>\d+(?:\.\d+)?)\s+Total:(?P<line_total>\d+(?:\.\d+)?)$"
)


def build_database(project_dir: Path, replace: bool) -> None:
    old_db = project_dir / "data.db"
    new_db = project_dir / "data_v2.db"
    schema_sql = project_dir / "database" / "schema_v2.sql"
    migration_sql = project_dir / "database" / "migration_v1_to_v2.sql"

    if not old_db.exists():
        raise FileNotFoundError(f"Missing source database: {old_db}")
    if new_db.exists() and not replace:
        raise FileExistsError(f"{new_db} already exists. Re-run with --replace to rebuild it.")
    if new_db.exists():
        new_db.unlink()

    with sqlite3.connect(new_db) as connection:
        connection.execute("PRAGMA foreign_keys = ON")
        connection.executescript(schema_sql.read_text(encoding="utf-8"))
        connection.execute("ATTACH DATABASE ? AS old", (str(old_db),))
        connection.executescript(migration_sql.read_text(encoding="utf-8"))
        enrich_bill_items_from_receipts(connection, project_dir)
        verify_counts(connection)


def enrich_bill_items_from_receipts(connection: sqlite3.Connection, project_dir: Path) -> None:
    bills = connection.execute("SELECT id, receipt_file FROM bills WHERE receipt_file IS NOT NULL").fetchall()
    for bill_id, receipt_file in bills:
        receipt_path = project_dir / receipt_file
        if not receipt_path.exists():
            continue

        parsed_items = []
        for line in receipt_path.read_text(encoding="utf-8", errors="replace").splitlines():
            match = RECEIPT_ITEM.match(line.strip())
            if not match:
                continue
            item = match.groupdict()
            product = connection.execute(
                "SELECT id, sector FROM products WHERE name = ? LIMIT 1",
                (item["name"],),
            ).fetchone()
            parsed_items.append(
                (
                    bill_id,
                    product[0] if product else None,
                    item["name"],
                    int(item["quantity"]),
                    item["unit_price"],
                    item["line_total"],
                    product[1] if product else None,
                    "LEGACY_RECEIPT_FILE",
                )
            )

        if parsed_items:
            connection.execute(
                "DELETE FROM bill_items WHERE bill_id = ? AND source = 'LEGACY_BILL_SUMMARY'",
                (bill_id,),
            )
            connection.executemany(
                """
                INSERT INTO bill_items (
                    bill_id,
                    product_id,
                    product_name,
                    quantity,
                    unit_price,
                    line_total,
                    sector,
                    source
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """,
                parsed_items,
            )


def verify_counts(connection: sqlite3.Connection) -> None:
    checks = {
        "employees": "SELECT (SELECT COUNT(*) FROM old.Cashier) + (SELECT COUNT(*) FROM old.Manager) + (SELECT COUNT(*) FROM old.Administrator)",
        "users": "SELECT (SELECT COUNT(*) FROM old.Cashier) + (SELECT COUNT(*) FROM old.Manager) + (SELECT COUNT(*) FROM old.Administrator)",
        "suppliers": "SELECT COUNT(*) FROM old.Supplier",
        "products": "SELECT COUNT(*) FROM old.Product",
        "bills": "SELECT COUNT(*) FROM old.Bill",
        "expenses": "SELECT COUNT(*) FROM old.expenses",
        "notifications": "SELECT COUNT(*) FROM old.Notifications",
        "login_history": "SELECT COUNT(*) FROM old.cashier_Login",
    }

    failures = []
    for table, old_count_sql in checks.items():
        old_count = connection.execute(old_count_sql).fetchone()[0]
        new_count = connection.execute(f"SELECT COUNT(*) FROM {table}").fetchone()[0]
        if old_count != new_count:
            failures.append(f"{table}: old={old_count}, new={new_count}")

    if failures:
        raise RuntimeError("Migration count mismatch: " + "; ".join(failures))


def main() -> None:
    parser = argparse.ArgumentParser(description="Build data_v2.db from the legacy data.db.")
    parser.add_argument("--replace", action="store_true", help="Replace an existing data_v2.db.")
    args = parser.parse_args()

    project_dir = Path(__file__).resolve().parents[1]
    build_database(project_dir, args.replace)
    print(f"Created {project_dir / 'data_v2.db'}")


if __name__ == "__main__":
    main()
