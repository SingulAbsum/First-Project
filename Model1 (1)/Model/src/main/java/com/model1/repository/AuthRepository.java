package main.java.com.model1.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import main.java.com.model1.model.dto.LoginRequest;
import main.java.com.model1.model.enums.Role;

public class AuthRepository {
    public boolean credentialsMatch(LoginRequest request) throws SQLException {
        Role role = request.role();
        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(selectSql(connection, role))) {
            statement.setString(1, request.username());
            statement.setString(2, request.password());
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() && resultSet.getInt(1) == 1;
            }
        }
    }

    private String selectSql(Connection connection, Role role) throws SQLException {
        if (tableExists(connection, "users")) {
            return switch (role) {
                case CASHIER -> """
                        SELECT COUNT(*)
                        FROM users
                        WHERE username=? AND password=? AND role_id=1 AND active=1 AND access_level>0
                        """;
                case MANAGER -> """
                        SELECT COUNT(*)
                        FROM users
                        WHERE username=? AND password=? AND role_id=2 AND active=1 AND access_level>1
                        """;
                case ADMINISTRATOR -> """
                        SELECT COUNT(*)
                        FROM users
                        WHERE username=? AND password=? AND role_id=3 AND active=1 AND access_level>2
                        """;
            };
        }

        return switch (role) {
            case CASHIER -> "SELECT COUNT(*) FROM Cashier WHERE cUsername=? AND cPassword=? AND cAccessLevel>0";
            case MANAGER -> "SELECT COUNT(*) FROM Manager WHERE mUsername=? AND mPassword=? AND mAccessLevel>1";
            case ADMINISTRATOR -> "SELECT COUNT(*) FROM Administrator WHERE aUsername=? AND aPassword=? AND aAccessLevel>2";
        };
    }

    private boolean tableExists(Connection connection, String tableName) throws SQLException {
        String sql = "SELECT 1 FROM sqlite_master WHERE type='table' AND lower(name)=lower(?) LIMIT 1";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, tableName);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }
}
