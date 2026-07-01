package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import main.java.com.model1.repository.DatabaseManager;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

public class Return {
	private static double total=0.0;
	public static int returnBillNr() {
		String query="SELECT MAX(billid) FROM bill";
		try (Connection line=DatabaseManager.getConnection();
				PreparedStatement args=line.prepareStatement(query)){
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
	public static double returnProductPrice(Connection line,String productName) {
		String query="SELECT productprice FROM product WHERE productname=?";
		try (PreparedStatement args=line.prepareStatement(query)){
			args.setString(1, productName);
			ResultSet result=args.executeQuery();
			if(result.next()) {
				return result.getDouble(1);
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	public static double returnProductPrice(String productName) {
		String query="SELECT productprice FROM product WHERE productname=?";
		try (Connection line=DatabaseManager.getConnection();
				PreparedStatement args=line.prepareStatement(query)){
			args.setString(1, productName);
			ResultSet result=args.executeQuery();
			if(result.next()) {
				return result.getDouble(1);
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	public static double getTotal() {
		return total;
	}
	public static void setTotal(double total) {
		Return.total = total;
	}
	public static int returnLastCashierIdActive(Connection line) {
		String query="SELECT MAX(loginid) FROM cashier_Login";
		try (PreparedStatement args=line.prepareStatement(query)){
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
	public static String returnLastCashierNameActive(Connection line) {
		String query="SELECT cashiername FROM cashier_Login ORDER BY loginid DESC LIMIT 1";
		try (PreparedStatement args=line.prepareStatement(query)){
			ResultSet result=args.executeQuery();
			if(result.next()) {
				return result.getString(1);
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static int returnTotalBills(String name,String start,String end) {
		String query="SELECT COUNT(cashier) FROM bill WHERE billdate>=? AND billdate<=? AND cashier=?";
		try (Connection line=DatabaseManager.getConnection();
				PreparedStatement args=line.prepareStatement(query)){
			args.setString(1, start);
			args.setString(2, end);
			args.setString(3, name);
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
	public static int returnTotalItems(String name,String start,String end) {
		String query="SELECT SUM(totalitems) FROM bill WHERE billdate>=? AND billdate<=? AND cashier=?";
		try (Connection line=DatabaseManager.getConnection();
				PreparedStatement args=line.prepareStatement(query)){
			args.setString(1, start);
			args.setString(2, end);
			args.setString(3, name);
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
	public static double returnTotalRevenue(String name,String start,String end) {
		String query="SELECT SUM(total) FROM bill WHERE billdate>=? AND billdate<=? AND cashier=?";
		try (Connection line=DatabaseManager.getConnection();
				PreparedStatement args=line.prepareStatement(query)){
			args.setString(1, start);
			args.setString(2, end);
			args.setString(3, name);
			ResultSet result=args.executeQuery();
			if(result.next()) {
				return result.getDouble(1);
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	public static int returnItemsSold() {
		String query="SELECT SUM(totalitems) FROM bill";
		try (Connection line=DatabaseManager.getConnection();
				PreparedStatement args=line.prepareStatement(query)){
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
	public static int returnItemsPurchased() {
		String query="SELECT SUM(itemspurchased) FROM expenses";
		try (Connection line=DatabaseManager.getConnection();
				PreparedStatement args=line.prepareStatement(query)){
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
	public static double returnItemsSoldRevenue() {
		String query="SELECT SUM(total) FROM bill";
		try (Connection line=DatabaseManager.getConnection();
				PreparedStatement args=line.prepareStatement(query)){
			ResultSet result=args.executeQuery();
			if(result.next()) {
				return result.getDouble(1);
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	public static double returnItemsPurchasedRevenue() {
		String query="SELECT SUM(expense) FROM expenses";
		try (Connection line=DatabaseManager.getConnection();
				PreparedStatement args=line.prepareStatement(query)){
			ResultSet result=args.executeQuery();
			if(result.next()) {
				return result.getDouble(1);
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	public static ArrayList<String> returnName(String role) {
		String query="SELECT "+role.charAt(0)+"name  FROM "+role;
		ArrayList<String> name=new ArrayList<>();
		try (Connection line=DatabaseManager.getConnection();
				PreparedStatement args=line.prepareStatement(query)){
			ResultSet result=args.executeQuery();
			while(result.next()) {
				name.add(result.getString(1));
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return name;
	}
	public static double returnFinalTotalExpense(String start,String end) {
		int month=0;
		int day=0;
		Integer fin=Integer.parseInt(end)-Integer.parseInt(start);
		if(fin>=10000000) {
			month=fin/1000000;
		}
	    day=(fin%1000000)/10000;
		double dayfinal=(double)day/100;
		int year=fin%10000;
		String cashier="SELECT SUM(csalary) FROM cashier";
		String manager="SELECT SUM(msalary) FROM manager";
		String administrator="SELECT SUM(asalary) FROM administrator";
		String expenses="SELECT SUM(expense) FROM expenses";
		try (Connection line=DatabaseManager.getConnection();
				PreparedStatement args=line.prepareStatement(cashier);
				PreparedStatement arg=line.prepareStatement(manager);
				PreparedStatement ar=line.prepareStatement(administrator);
				PreparedStatement a=line.prepareStatement(expenses)){
			ResultSet result=args.executeQuery();
			ResultSet resul=arg.executeQuery();
			ResultSet resu=ar.executeQuery();
			ResultSet res=a.executeQuery();
			if(result.next() && resul.next() && resu.next() && res.next()) {
				return (result.getDouble(1)*month+result.getDouble(1)*dayfinal+result.getDouble(1)*year+resul.getDouble(1)*month+resul.getDouble(1)*dayfinal+resul.getDouble(1)*year+resu.getDouble(1)*month+resu.getDouble(1)*dayfinal+resu.getDouble(1)*year+res.getDouble(1));
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	public static double returnItemsSoldRevenueTimed(String start,String end) {
		String query="SELECT SUM(total) FROM bill WHERE billdate>=? AND billdate<=?";
		try (Connection line=DatabaseManager.getConnection();
				PreparedStatement args=line.prepareStatement(query)){
			args.setString(1, start);
			args.setString(2, end);
			ResultSet result=args.executeQuery();
			if(result.next()) {
				return result.getDouble(1);
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	public static ObservableList<String> returnAllProductNames() {
		ObservableList<String> names=FXCollections.observableArrayList();
		String query="SELECT productname FROM product";
		try (Connection line=DatabaseManager.getConnection();
				PreparedStatement args=line.prepareStatement(query)){
			ResultSet result=args.executeQuery();
			while(result.next()) {
				names.add(result.getString(1));
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return names;
	}
}
