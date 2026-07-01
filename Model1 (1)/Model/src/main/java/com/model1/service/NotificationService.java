package main.java.com.model1.service;

import java.sql.SQLException;
import java.util.List;

import main.java.com.model1.exception.DatabaseException;
import main.java.com.model1.model.entity.Notification;
import main.java.com.model1.repository.DatabaseManager;
import main.java.com.model1.repository.NotificationRepository;
import main.java.com.model1.util.ValidationUtils;

public class NotificationService {
    private final NotificationRepository notificationRepository;

    public NotificationService() {
        this(new NotificationRepository());
    }

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public List<Notification> findBySector(int sector) {
        try {
            return notificationRepository.findBySector(sector);
        } catch (SQLException ex) {
            throw new DatabaseException("Unable to load notifications.", ex);
        }
    }

    public void resolve(Notification notification) {
        ValidationUtils.requireNonNull(notification, "Notification");

        DatabaseManager.inTransaction(connection -> {
            notificationRepository.delete(connection, notification);
        });
    }
}
