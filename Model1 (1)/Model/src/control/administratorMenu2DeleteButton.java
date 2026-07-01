package control;

public class administratorMenu2DeleteButton {
	public static void edit(String role,String name) {
		if(role.equalsIgnoreCase(null)||name.equalsIgnoreCase(null)) {
			view.Cashier.emptyFields();
		}
		else if(model.updateInsert.delete(role,name)) {
			view.Cashier.done();
		}
		else {
			view.Cashier.productAddError();
		}
	}
}
