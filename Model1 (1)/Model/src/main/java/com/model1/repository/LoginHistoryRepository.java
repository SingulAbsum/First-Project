package main.java.com.model1.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import main.java.com.model1.model.entity.LoginHistory;

public class LoginHistoryRepository {
    public Optional<LoginHistory> findLatestCashierLogin(Connection connection) throws SQLException {
        String sql = hasModernSchema(connection)
                ? "SELECT id, cashier_name FROM login_history ORDER BY id DESC LIMIT 1"
                : "SELECT loginId, cashierName FROM cashier_Login ORDER BY loginId DESC LIMIT 1";
        try (PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return Optional.of(new LoginHistory(
                        resultSet.getInt(1),
                        resultSet.getString(2)));
            }
        }
        return Optional.empty();
    }

    public int nextLoginId(Connection connection) throws SQLException {
        String sql = hasModernSchema(connection)
                ? "SELECT MAX(id) FROM login_history"
                : "SELECT MAX(loginId) FROM cashier_Login";
        try (PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {
            return resultSet.next() ? resultSet.getInt(1) + 1 : 1;
        }
    }

    public int save(Connection connection, String cashierName) throws SQLException {
        if (hasModernSchema(connection)) {
            return saveModern(connection, cashierName);
        }

        String sql = "INSERT INTO cashier_Login(cashierName, loginId) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, cashierName);
            statement.setInt(2, nextLoginId(connection));
            return statement.executeUpdate();
        }
    }

    private int saveModern(Connection connection, String cashierName) throws SQLException {
        String sql = """
                INSERT INTO login_history(id, user_id, cashier_name, login_at)
                VALUES (?, ?, ?, CURRENT_TIMESTAMP)
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, nextLoginId(connection));
            Integer userId = findCashierUserId(connection, cashierName);
            if (userId == null) {
                statement.setNull(2, java.sql.Types.INTEGER);
            } else {
                statement.setInt(2, userId);
            }
            statement.setString(3, cashierName);
            return statement.executeUpdate();
        }
    }

    private Integer findCashierUserId(Connection connection, String username) throws SQLException {
        String sql = "SELECT id FROM users WHERE username=? AND role_id=1 LIMIT 1";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? resultSet.getInt("id") : null;
            }
        }
    }

    private boolean hasModernSchema(Connection connection) throws SQLException {
        String sql = "SELECT 1 FROM sqlite_master WHERE type='table' AND lower(name)=lower('login_history') LIMIT 1";
        try (PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {
            return resultSet.next();
        }
    }
}
