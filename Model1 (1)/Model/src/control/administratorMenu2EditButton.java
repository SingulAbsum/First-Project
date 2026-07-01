package control;

public class administratorMenu2EditButton {
	public static void edit(String role,String field,String newField,String name) {
		if(role.equalsIgnoreCase(null)||field.equalsIgnoreCase(null)||newField.equalsIgnoreCase(null)||name.equalsIgnoreCase(null)) {
			view.Cashier.emptyFields();
		}
		else if(model.updateInsert.edit(role,field,newField,name)) {
			view.Cashier.done();
		}
		else {
			view.Cashier.productAddError();
		}
	}
}
