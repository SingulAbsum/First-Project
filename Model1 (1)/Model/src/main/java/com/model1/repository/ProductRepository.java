package main.java.com.model1.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import main.java.com.model1.model.entity.Product;

public class ProductRepository {
    public List<Product> findAll() throws SQLException {
        String sql = "SELECT productname, quantity, productprice, supplierid, sector FROM product ORDER BY productname";
        List<Product> products = new ArrayList<>();

        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                products.add(mapProduct(resultSet));
            }
        }
        return products;
    }

    public List<String> findAllNames() throws SQLException {
        String sql = "SELECT productname FROM product ORDER BY productname";
        List<String> names = new ArrayList<>();

        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                names.add(resultSet.getString("productname"));
            }
        }
        return names;
    }

    public Optional<Product> findByNameAndSector(String productName, int sector) throws SQLException {
        String sql = "SELECT productname, quantity, productprice, supplierid, sector FROM product WHERE productname=? AND sector=?";

        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, productName);
            statement.setInt(2, sector);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapProduct(resultSet));
                }
            }
        }
        return Optional.empty();
    }

    public Optional<Product> findByNameAndSector(Connection connection, String productName, int sector) throws SQLException {
        String sql = "SELECT productname, quantity, productprice, supplierid, sector FROM product WHERE productname=? AND sector=?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, productName);
            statement.setInt(2, sector);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapProduct(resultSet));
                }
            }
        }
        return Optional.empty();
    }

    public boolean hasAvailableQuantity(String productName, int quantity, int sector) throws SQLException {
        String sql = "SELECT COUNT(*) FROM product WHERE productname=? AND quantity>=? AND sector=?";

        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, productName);
            statement.setInt(2, quantity);
            statement.setInt(3, sector);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() && resultSet.getInt(1) == 1;
            }
        }
    }

    public int decreaseQuantity(Connection connection, String productName, int quantity, int sector) throws SQLException {
        String sql = "UPDATE product SET quantity=quantity-? WHERE productname=? AND sector=? AND quantity>=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, quantity);
            statement.setString(2, productName);
            statement.setInt(3, sector);
            statement.setInt(4, quantity);
            return statement.executeUpdate();
        }
    }

    public int increaseQuantity(Connection connection, String productName, int quantity, int sector) throws SQLException {
        String sql = "UPDATE product SET quantity=quantity+? WHERE productname=? AND sector=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, quantity);
            statement.setString(2, productName);
            statement.setInt(3, sector);
            return statement.executeUpdate();
        }
    }

    public int save(Connection connection, Product product) throws SQLException {
        String sql = "INSERT INTO product(productname, quantity, productprice, supplierid, sector) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, product.name());
            statement.setInt(2, product.quantity());
            statement.setBigDecimal(3, product.price());
            statement.setInt(4, product.supplierId());
            statement.setInt(5, product.sector());
            return statement.executeUpdate();
        }
    }

    private Product mapProduct(ResultSet resultSet) throws SQLException {
        return new Product(
                resultSet.getString("productname"),
                resultSet.getInt("quantity"),
                RepositoryMappers.decimal(resultSet, "productprice"),
                resultSet.getInt("supplierid"),
                resultSet.getInt("sector"));
    }
}
