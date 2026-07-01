package control;

public class administratorMenu1AddAdministratorButton {
	public static void addAdministrator(String id,String name,String birthdate,String phone,String email,String salary,String accessLevel,String username,String password,String sector) {
		if(id.equalsIgnoreCase("")||name.equalsIgnoreCase("")||birthdate.equalsIgnoreCase("")||phone.equalsIgnoreCase("")||email.equalsIgnoreCase("")||salary.equalsIgnoreCase("")||accessLevel.equalsIgnoreCase("")||username.equalsIgnoreCase("")||password.equalsIgnoreCase("")||sector.equalsIgnoreCase("")) {
			view.Cashier.emptyFields();
		}
		try {
		if(model.updateInsert.insertNewAdministrator(Integer.parseInt(id),name,birthdate, Integer.parseInt(phone),email,Double.parseDouble(salary),Integer.parseInt(accessLevel),username,password,Integer.parseInt(sector))) {
			view.Cashier.done();
		}
		}
		catch(NumberFormatException n) {
			view.Cashier.productAddError();
		}
	}
}
