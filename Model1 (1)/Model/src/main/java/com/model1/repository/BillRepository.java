package main.java.com.model1.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import main.java.com.model1.model.entity.Bill;

public class BillRepository {
    public int nextBillId() throws SQLException {
        String sql = "SELECT MAX(billid) FROM bill";
        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {
            return resultSet.next() ? resultSet.getInt(1) + 1 : 1;
        }
    }

    public int nextBillId(Connection connection) throws SQLException {
        String sql = "SELECT MAX(billid) FROM bill";
        try (PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {
            return resultSet.next() ? resultSet.getInt(1) + 1 : 1;
        }
    }

    public int save(Connection connection, Bill bill) throws SQLException {
        String sql = "INSERT INTO bill(billid, billdate, total, filename, cashier, totalitems) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, bill.id());
            statement.setString(2, RepositoryMappers.formatLegacyDate(bill.date()));
            statement.setBigDecimal(3, bill.total());
            statement.setString(4, bill.fileName());
            statement.setString(5, bill.cashierName());
            statement.setInt(6, bill.totalItems());
            return statement.executeUpdate();
        }
    }

    public List<Bill> findByDate(LocalDate date) throws SQLException {
        String sql = "SELECT billid, billdate, total, filename, cashier, totalitems FROM bill WHERE billdate=?";
        List<Bill> bills = new ArrayList<>();

        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, RepositoryMappers.formatLegacyDate(date));
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    bills.add(mapBill(resultSet));
                }
            }
        }
        return bills;
    }

    public List<Bill> findAll() throws SQLException {
        String sql = "SELECT billid, billdate, total, filename, cashier, totalitems FROM bill ORDER BY billid DESC";
        List<Bill> bills = new ArrayList<>();

        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                bills.add(mapBill(resultSet));
            }
        }
        return bills;
    }

    public Optional<Bill> findById(int id) throws SQLException {
        String sql = "SELECT billid, billdate, total, filename, cashier, totalitems FROM bill WHERE billid=?";

        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapBill(resultSet));
                }
            }
        }
        return Optional.empty();
    }

    public int countByCashierAndDateRange(String cashierName, LocalDate startDate, LocalDate endDate) throws SQLException {
        String sql = "SELECT COUNT(cashier) FROM bill WHERE billdate>=? AND billdate<=? AND cashier=?";
        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, RepositoryMappers.formatLegacyDate(startDate));
            statement.setString(2, RepositoryMappers.formatLegacyDate(endDate));
            statement.setString(3, cashierName);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? resultSet.getInt(1) : 0;
            }
        }
    }

    public int totalItemsByCashierAndDateRange(String cashierName, LocalDate startDate, LocalDate endDate) throws SQLException {
        String sql = "SELECT SUM(totalitems) FROM bill WHERE billdate>=? AND billdate<=? AND cashier=?";
        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, RepositoryMappers.formatLegacyDate(startDate));
            statement.setString(2, RepositoryMappers.formatLegacyDate(endDate));
            statement.setString(3, cashierName);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? resultSet.getInt(1) : 0;
            }
        }
    }

    public java.math.BigDecimal revenueByCashierAndDateRange(String cashierName, LocalDate startDate, LocalDate endDate) throws SQLException {
        String sql = "SELECT SUM(total) AS revenue FROM bill WHERE billdate>=? AND billdate<=? AND cashier=?";
        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, RepositoryMappers.formatLegacyDate(startDate));
            statement.setString(2, RepositoryMappers.formatLegacyDate(endDate));
            statement.setString(3, cashierName);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? RepositoryMappers.decimal(resultSet, "revenue") : java.math.BigDecimal.ZERO;
            }
        }
    }

    public int totalItemsSold() throws SQLException {
        String sql = "SELECT SUM(totalitems) FROM bill";
        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {
            return resultSet.next() ? resultSet.getInt(1) : 0;
        }
    }

    public int totalItemsSoldByDateRange(LocalDate startDate, LocalDate endDate) throws SQLException {
        String sql = "SELECT SUM(totalitems) FROM bill WHERE billdate>=? AND billdate<=?";
        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, RepositoryMappers.formatLegacyDate(startDate));
            statement.setString(2, RepositoryMappers.formatLegacyDate(endDate));
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? resultSet.getInt(1) : 0;
            }
        }
    }

    public java.math.BigDecimal totalRevenue() throws SQLException {
        String sql = "SELECT SUM(total) AS revenue FROM bill";
        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {
            return resultSet.next() ? RepositoryMappers.decimal(resultSet, "revenue") : java.math.BigDecimal.ZERO;
        }
    }

    public java.math.BigDecimal revenueByDateRange(LocalDate startDate, LocalDate endDate) throws SQLException {
        String sql = "SELECT SUM(total) AS revenue FROM bill WHERE billdate>=? AND billdate<=?";
        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, RepositoryMappers.formatLegacyDate(startDate));
            statement.setString(2, RepositoryMappers.formatLegacyDate(endDate));
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? RepositoryMappers.decimal(resultSet, "revenue") : java.math.BigDecimal.ZERO;
            }
        }
    }

    private Bill mapBill(ResultSet resultSet) throws SQLException {
        return new Bill(
                resultSet.getInt("billid"),
                RepositoryMappers.legacyDate(resultSet, "billdate"),
                RepositoryMappers.decimal(resultSet, "total"),
                resultSet.getString("filename"),
                resultSet.getString("cashier"),
                resultSet.getInt("totalitems"),
                List.of());
    }
}
