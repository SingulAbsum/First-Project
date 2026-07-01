package main.java.com.model1.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import main.java.com.model1.model.entity.Notification;

public class NotificationRepository {
    public List<Notification> findBySector(int sector) throws SQLException {
        String sql = "SELECT notificationproduct, notificationsector FROM notifications WHERE notificationsector=?";
        List<Notification> notifications = new ArrayList<>();

        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, sector);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    notifications.add(new Notification(
                            resultSet.getString("notificationproduct"),
                            resultSet.getInt("notificationsector")));
                }
            }
        }
        return notifications;
    }

    public int save(Connection connection, Notification notification) throws SQLException {
        String sql = "INSERT INTO notifications(notificationproduct, notificationsector) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, notification.productName());
            statement.setInt(2, notification.sector());
            return statement.executeUpdate();
        }
    }

    public int delete(Connection connection, Notification notification) throws SQLException {
        String sql = "DELETE FROM notifications WHERE notificationproduct=? AND notificationsector=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, notification.productName());
            statement.setInt(2, notification.sector());
            return statement.executeUpdate();
        }
    }
}
