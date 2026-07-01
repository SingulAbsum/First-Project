package control;

public class managerMenu4RefillButton {
public static void refill(String name,String quantity,String sector) {
	if(quantity.equalsIgnoreCase("")) {
		view.Cashier.emptyFields();
	}
	try { if(model.updateInsert.refill(name, Integer.parseInt(quantity), Integer.parseInt(sector))) {
		view.Cashier.done();
		model.updateInsert.clear(name, Integer.parseInt(sector));
	}
	}
	catch(NumberFormatException n) {
		view.Cashier.productAddError();
	}
	
}
}
