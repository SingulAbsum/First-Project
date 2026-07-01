package main.java.com.model1.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import main.java.com.model1.model.entity.Supplier;

public class SupplierRepository {
    public int nextSupplierId() throws SQLException {
        String sql = "SELECT MAX(supplierid) FROM supplier";
        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {
            return resultSet.next() ? resultSet.getInt(1) + 1 : 1;
        }
    }

    public int nextSupplierId(Connection connection) throws SQLException {
        String sql = "SELECT MAX(supplierid) FROM supplier";
        try (PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {
            return resultSet.next() ? resultSet.getInt(1) + 1 : 1;
        }
    }

    public List<Supplier> findAll() throws SQLException {
        String sql = "SELECT supplierid, suppliername, suppliercategory FROM supplier ORDER BY suppliername";
        List<Supplier> suppliers = new ArrayList<>();

        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                suppliers.add(mapSupplier(resultSet));
            }
        }
        return suppliers;
    }

    public Optional<Supplier> findById(int id) throws SQLException {
        String sql = "SELECT supplierid, suppliername, suppliercategory FROM supplier WHERE supplierid=?";

        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapSupplier(resultSet));
                }
            }
        }
        return Optional.empty();
    }

    public int save(Connection connection, Supplier supplier) throws SQLException {
        String sql = "INSERT INTO supplier(supplierid, suppliername, suppliercategory) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, supplier.id());
            statement.setString(2, supplier.name());
            statement.setString(3, supplier.category());
            return statement.executeUpdate();
        }
    }

    private Supplier mapSupplier(ResultSet resultSet) throws SQLException {
        return new Supplier(
                resultSet.getInt("supplierid"),
                resultSet.getString("suppliername"),
                resultSet.getString("suppliercategory"));
    }
}
