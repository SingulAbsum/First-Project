package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import main.java.com.model1.repository.DatabaseManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Get {
	public static ObservableList<String> getFileNames() {
		ObservableList<String> fileNames=FXCollections.observableArrayList();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddyyyy");
		String formattedDate = LocalDateTime.now().format(formatter);
		String query="SELECT filename FROM bill WHERE billdate==?";
		try (Connection line=DatabaseManager.getConnection();
				PreparedStatement args=line.prepareStatement(query)){
		args.setString(1, formattedDate);
		ResultSet result=args.executeQuery();
		 while (result.next()) {
	            fileNames.add(result.getString("filename"));
	        }
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return fileNames;
	}
	public static int getTotalBills() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddyyyy");
		String formattedDate = LocalDateTime.now().format(formatter);
		String query="SELECT SUM(total) FROM bill WHERE billdate==?";
		try (Connection line=DatabaseManager.getConnection();
				PreparedStatement args=line.prepareStatement(query)){
		args.setString(1, formattedDate);
		ResultSet result=args.executeQuery();
		if(result.next()) {
			return result.getInt(1);
		}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	public static ObservableList<String> getNotifications(int sector) {
		ObservableList<String> notifications=FXCollections.observableArrayList();
		String query="SELECT notificationproduct FROM notifications WHERE notificationsector==?";
		try (Connection line=DatabaseManager.getConnection();
				PreparedStatement args=line.prepareStatement(query)){
		args.setInt(1, sector);
		ResultSet result=args.executeQuery();
		 while (result.next()) {
	            notifications.add(result.getString("notificationproduct"));
	        }
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return notifications;
	}
}
