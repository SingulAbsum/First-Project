package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import main.java.com.model1.repository.DatabaseManager;

public class Database{
	public static boolean checkCredentialsCashier(String username, String password) {
		String query="SELECT COUNT(*) FROM cashier WHERE cusername=? AND cpassword=? AND caccesslevel>0";
		try (Connection line=DatabaseManager.getConnection();
				PreparedStatement args=line.prepareStatement(query)){
			args.setString(1, username);
			args.setString(2, password);
			ResultSet result=args.executeQuery();
			if(result.next()) {
				return result.getInt(1)==1;
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return false;

}
	public static boolean checkCredentialsManager(String username, String password) {
		String query="SELECT COUNT(*) FROM manager WHERE musername=? AND mpassword=? AND maccesslevel>1";
		try (Connection line=DatabaseManager.getConnection();
				PreparedStatement args=line.prepareStatement(query)){
			args.setString(1, username);
			args.setString(2, password);
			ResultSet result=args.executeQuery();
			if(result.next()) {
				return result.getInt(1)==1;
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	public static boolean checkCredentialsAdministrator(String username, String password) {
		String query="SELECT COUNT(*) FROM administrator WHERE ausername=? AND apassword=? AND aaccesslevel>2";
		try (Connection line=DatabaseManager.getConnection();
				PreparedStatement args=line.prepareStatement(query)){
			args.setString(1, username);
			args.setString(2, password);
			ResultSet result=args.executeQuery();
			if(result.next()) {
				return result.getInt(1)==1;
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	public static boolean checkProduct(String productName, int quantity,int sector) {
		if (productName == null || productName.isEmpty() || quantity <= 0) {
	        return false; // Invalid input
		}
		String query="SELECT COUNT(*),quantity FROM product WHERE productname=? AND quantity>=? AND sector=?";
		try (Connection line=DatabaseManager.getConnection();
				PreparedStatement args=line.prepareStatement(query)){
			args.setString(1, productName);
			args.setInt(2, quantity);
			args.setInt(3, sector);
			ResultSet result=args.executeQuery();
			
			if(result.next()) {
				
				if(result.getInt(2)<=5) {
	updateInsert.addNotification(line,productName, sector);
			}
				return result.getInt(1)==1;
			}
		}
		catch(SQLException e) {
			return false;
		}
		return false;
	}
}
