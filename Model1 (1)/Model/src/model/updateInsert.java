package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import main.java.com.model1.repository.DatabaseManager;

public class updateInsert {
	public static int updateProductQuantity(String productName, int quantity,int sector) {
		String query="UPDATE product SET quantity=quantity-? WHERE productname=? AND sector=?";
		try (Connection line=DatabaseManager.getConnection();
				PreparedStatement args=line.prepareStatement(query)){
			args.setInt(1, quantity);
			args.setString(2, productName);
			args.setInt(3, sector);
			int result=args.executeUpdate();
			if(result==1) {
				return 1;
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	public static boolean insertBill(int billNr,String date,String fileName,double total,int totalItems) {
		String query="INSERT INTO bill(billid,billdate,total,filename,cashier,totalitems) VALUES (?,?,?,?,?,?)";
		try (Connection line=DatabaseManager.getConnection();
				PreparedStatement args=line.prepareStatement(query)){
			args.setInt(1, billNr);
			args.setString(2, date);
			args.setDouble(3, total);
			args.setString(4, fileName);
			args.setString(5, model.Return.returnLastCashierNameActive(line));
			args.setInt(6, totalItems);
			int result=args.executeUpdate();
			if(result==1) {
				return true;
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	public static boolean insertNewProductCategory(String productName,int quantity,double price,int sector,int supplierId,String supplierName,String supplierCategory) {
		String query="INSERT INTO product(productname,quantity,productprice,sector,supplierid) VALUES (?,?,?,?,?)";
		String category="INSERT INTO supplier(supplierid,suppliername,suppliercategory) VALUES (?,?,?)";
		String expense="INSERT INTO expenses(expensedate,itemspurchased,expense,expenseSector) VALUES (?,?,?,?)";
		try (Connection line=DatabaseManager.getConnection();
				PreparedStatement args=line.prepareStatement(query);
				PreparedStatement arg=line.prepareStatement(category);
				PreparedStatement ar=line.prepareStatement(expense);){
			args.setString(1, productName);
			args.setInt(2, quantity);
			args.setDouble(3, price);
			args.setInt(4, sector);
			args.setInt(5, supplierId);
			arg.setInt(1, supplierId);
			arg.setString(2, supplierName);
			arg.setString(3, supplierCategory);
			ar.setString(1, LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMddyyyy")));
			ar.setInt(2, quantity);
			ar.setDouble(3, (double)price*quantity);
			ar.setInt(4, sector);
			int result=args.executeUpdate();
			int res=arg.executeUpdate();
			int re=ar.executeUpdate();
			if(result==1 && res==1 && re==1) {
				return true;
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	public static boolean insertNewProduct(String productName,int quantity,double price,int sector,int supplierId) {
		String query="INSERT INTO product(productname,quantity,productprice,sector,supplierid) VALUES (?,?,?,?,?)";
		String expense="INSERT INTO expenses(expensedate,itemspurchased,expense,expenseSector) VALUES (?,?,?,?)";
		try (Connection line=DatabaseManager.getConnection();
				PreparedStatement args=line.prepareStatement(query);
				PreparedStatement arg=line.prepareStatement(expense);){
			args.setString(1, productName);
			args.setInt(2, quantity);
			args.setDouble(3, price);
			args.setInt(4, sector);
			args.setInt(5, supplierId);
			arg.setString(1, LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMddyyyy")));
			arg.setInt(2, quantity);
			arg.setDouble(3, (double)price*quantity);
			arg.setInt(4, sector);
			int result=args.executeUpdate();
			int res=arg.executeUpdate();
			if(result==1 && res==1) {
				return true;
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
public static boolean addNotification(Connection line,String notificationProduct,int notificationSector) {
		String query="INSERT INTO notifications(notificationproduct,notificationsector) VALUES (?,?)";
		try (PreparedStatement args=line.prepareStatement(query);){
			args.setString(1, notificationProduct);
			args.setInt(2, notificationSector);
			int result=args.executeUpdate();
			if(result==1) {
				return true;
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
public static boolean refill(String name, int quantity,int sector) {
	String query="UPDATE product SET quantity=quantity+? WHERE productname=? AND sector=?";
	String expense="INSERT INTO expenses(expensedate,itemspurchased,expense,expenseSector) VALUES (?,?,?,?)";
	try (Connection line=DatabaseManager.getConnection();
			PreparedStatement args=line.prepareStatement(query);
			PreparedStatement arg=line.prepareStatement(expense);){
		args.setInt(1, quantity);
		args.setString(2, name);
		args.setInt(3, sector);
		arg.setString(1, LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMddyyyy")));
		arg.setInt(2, quantity);
		arg.setDouble(3, (double)model.Return.returnProductPrice(line, name)*quantity);
		arg.setInt(4, sector);
		int result=args.executeUpdate();
		int res=arg.executeUpdate();
		if(result==1 && res==1) {
			return true;
		}
	}
	catch(SQLException e) {
		e.printStackTrace();
	}
	return false;
}
public static boolean clear(String name,int sector) {
	String query="DELETE FROM notifications WHERE notificationproduct=? AND notificationsector=?";
	try (Connection line=DatabaseManager.getConnection();
			PreparedStatement args=line.prepareStatement(query)){
		args.setString(1, name);
		args.setInt(2, sector);
		int result=args.executeUpdate();
		if(result>0) {
			return true;
		}
	}
	catch(SQLException e) {
		e.printStackTrace();
	}
	return false;
}
public static boolean insertNewCashierActivity(String name) {
	String query="INSERT INTO cashier_login(cashierName,loginid) VALUES (?,?)";
	try (Connection line=DatabaseManager.getConnection();
			PreparedStatement args=line.prepareStatement(query);){
		args.setString(1, name);
		args.setInt(2, model.Return.returnLastCashierIdActive(line)+1);
		int result=args.executeUpdate();
		if(result==1) {
			return true;
		}
	}
	catch(SQLException e) {
		e.printStackTrace();
	}
	return false;
}
public static boolean insertNewCashier(int id,String name,String birthdate,int phone,String email,double salary,int accessLevel,String username,String password,int sector) {
	String query="INSERT INTO cashier(cid,cname,cbirthdate,cphonenumber,email,csalary,caccesslevel,cusername,cpassword,sector) VALUES (?,?,?,?,?,?,?,?,?,?)";
	try (Connection line=DatabaseManager.getConnection();
			PreparedStatement args=line.prepareStatement(query);){
		args.setInt(1, id);
		args.setString(2, name);
		args.setString(3, birthdate);
		args.setInt(4, phone);
		args.setString(5, email);
		args.setDouble(6, salary);
		args.setInt(7, accessLevel);
		args.setString(8, username);
		args.setString(9, password);
		args.setInt(10, sector);
		int result=args.executeUpdate();
		if(result==1) {
			return true;
		}
	}
	catch(SQLException e) {
		e.printStackTrace();
	}
	return false;
}
public static boolean insertNewManager(int id,String name,String birthdate,int phone,String email,double salary,int accessLevel,String username,String password,int sector) {
	String query="INSERT INTO manager(mid,mname,mbirthdate,mphonenumber,memail,msalary,maccesslevel,musername,mpassword,sector) VALUES (?,?,?,?,?,?,?,?,?,?)";
	try (Connection line=DatabaseManager.getConnection();
			PreparedStatement args=line.prepareStatement(query);){
		args.setInt(1, id);
		args.setString(2, name);
		args.setString(3, birthdate);
		args.setInt(4, phone);
		args.setString(5, email);
		args.setDouble(6, salary);
		args.setInt(7, accessLevel);
		args.setString(8, username);
		args.setString(9, password);
		args.setInt(10, sector);
		int result=args.executeUpdate();
		if(result==1) {
			return true;
		}
	}
	catch(SQLException e) {
		e.printStackTrace();
	}
	return false;
}
public static boolean insertNewAdministrator(int id,String name,String birthdate,int phone,String email,double salary,int accessLevel,String username,String password,int sector) {
	String query="INSERT INTO administrator(aid,aname,abirthdate,aphonenumber,aemail,asalary,aaccesslevel,ausername,apassword,sector) VALUES (?,?,?,?,?,?,?,?,?,?)";
	try (Connection line=DatabaseManager.getConnection();
			PreparedStatement args=line.prepareStatement(query);){
		args.setInt(1, id);
		args.setString(2, name);
		args.setString(3, birthdate);
		args.setInt(4, phone);
		args.setString(5, email);
		args.setDouble(6, salary);
		args.setInt(7, accessLevel);
		args.setString(8, username);
		args.setString(9, password);
		args.setInt(10, sector);
		int result=args.executeUpdate();
		if(result==1) {
			return true;
		}
	}
	catch(SQLException e) {
		e.printStackTrace();
	}
	return false;
}
public static boolean edit(String role,String field,String newField,String name) {
	String query = "UPDATE " + role + " SET " + role.charAt(0) + field + " = ? WHERE " + role.charAt(0) + "name = ?";
     try (Connection line=DatabaseManager.getConnection();
			PreparedStatement args=line.prepareStatement(query);){
    	 args.setDouble(1, Double.parseDouble(newField));
    	 args.setString(2, name);
		int result=args.executeUpdate();
		if(result==1) {
			return true;
		}
	}
	catch(SQLException e) {
		e.printStackTrace();
	}
	return false;
}
public static boolean delete(String role,String name) {
	String query="DELETE FROM "+role+" WHERE "+role.charAt(0)+"name=?";
	try (Connection line=DatabaseManager.getConnection();
			PreparedStatement args=line.prepareStatement(query)){
		args.setString(1, name);
		int result=args.executeUpdate();
		if(result>0) {
			return true;
		}
	}
	catch(SQLException e) {
		e.printStackTrace();
	}
	return false;
}
}
