package main.java.com.model1.repository;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import main.java.com.model1.model.entity.BillItem;

public class BillItemRepository {
    public void ensureTable(Connection connection) throws SQLException {
        if (hasTable(connection, "bill_items")) {
            return;
        }

        String sql = """
                CREATE TABLE IF NOT EXISTS bill_items (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    billid INTEGER NOT NULL,
                    productname TEXT NOT NULL,
                    quantity INTEGER NOT NULL,
                    unitprice NUMERIC NOT NULL,
                    linetotal NUMERIC NOT NULL,
                    sector INTEGER NOT NULL
                )
                """;
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    public int saveAll(Connection connection, int billId, List<BillItem> items) throws SQLException {
        ensureTable(connection);
        if (usesNormalizedColumns(connection)) {
            return saveAllNormalized(connection, billId, items);
        }
        return saveAllLegacy(connection, billId, items);
    }

    public List<BillItem> findByBillId(int billId) throws SQLException {
        try (Connection connection = DatabaseManager.getConnection()) {
            return findByBillId(connection, billId);
        }
    }

    public List<BillItem> findByBillId(Connection connection, int billId) throws SQLException {
        ensureTable(connection);
        if (usesNormalizedColumns(connection)) {
            return findByBillIdNormalized(connection, billId);
        }
        return findByBillIdLegacy(connection, billId);
    }

    private int saveAllLegacy(Connection connection, int billId, List<BillItem> items) throws SQLException {
        String sql = "INSERT INTO bill_items(billid, productname, quantity, unitprice, linetotal, sector) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (BillItem item : items) {
                statement.setInt(1, billId);
                statement.setString(2, item.productName());
                statement.setInt(3, item.quantity());
                statement.setBigDecimal(4, item.unitPrice());
                statement.setBigDecimal(5, item.lineTotal());
                statement.setInt(6, item.sector());
                statement.addBatch();
            }
            return sum(statement.executeBatch());
        }
    }

    private int saveAllNormalized(Connection connection, int billId, List<BillItem> items) throws SQLException {
        String sql = "INSERT INTO bill_items(bill_id, product_name, quantity, unit_price, line_total, sector) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (BillItem item : items) {
                statement.setInt(1, billId);
                statement.setString(2, item.productName());
                statement.setInt(3, item.quantity());
                statement.setBigDecimal(4, item.unitPrice());
                statement.setBigDecimal(5, item.lineTotal());
                statement.setInt(6, item.sector());
                statement.addBatch();
            }
            return sum(statement.executeBatch());
        }
    }

    private List<BillItem> findByBillIdLegacy(Connection connection, int billId) throws SQLException {
        String sql = "SELECT productname, quantity, unitprice, sector FROM bill_items WHERE billid=? ORDER BY id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, billId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return mapItems(resultSet, "productname", "unitprice");
            }
        }
    }

    private List<BillItem> findByBillIdNormalized(Connection connection, int billId) throws SQLException {
        String sql = "SELECT product_name, quantity, unit_price, sector FROM bill_items WHERE bill_id=? ORDER BY id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, billId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return mapItems(resultSet, "product_name", "unit_price");
            }
        }
    }

    private List<BillItem> mapItems(ResultSet resultSet, String productColumn, String priceColumn) throws SQLException {
        List<BillItem> items = new ArrayList<>();
        while (resultSet.next()) {
            items.add(new BillItem(
                    resultSet.getString(productColumn),
                    resultSet.getInt("quantity"),
                    RepositoryMappers.decimal(resultSet, priceColumn),
                    resultSet.getInt("sector")));
        }
        return items;
    }

    private boolean usesNormalizedColumns(Connection connection) throws SQLException {
        return hasColumn(connection, "bill_items", "bill_id");
    }

    private boolean hasTable(Connection connection, String tableName) throws SQLException {
        DatabaseMetaData metadata = connection.getMetaData();
        try (ResultSet resultSet = metadata.getTables(null, null, tableName, null)) {
            if (resultSet.next()) {
                return true;
            }
        }
        try (ResultSet resultSet = metadata.getTables(null, null, tableName.toUpperCase(), null)) {
            return resultSet.next();
        }
    }

    private boolean hasColumn(Connection connection, String tableName, String columnName) throws SQLException {
        DatabaseMetaData metadata = connection.getMetaData();
        try (ResultSet resultSet = metadata.getColumns(null, null, tableName, columnName)) {
            if (resultSet.next()) {
                return true;
            }
        }
        try (ResultSet resultSet = metadata.getColumns(null, null, tableName, columnName.toUpperCase())) {
            return resultSet.next();
        }
    }

    private int sum(int[] updates) {
        int total = 0;
        for (int update : updates) {
            if (update > 0) {
                total += update;
            }
        }
        return total;
    }
}
