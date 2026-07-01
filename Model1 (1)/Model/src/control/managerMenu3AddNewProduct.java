package control;

public class managerMenu3AddNewProduct {
	public static void addNewProduct(String productName,String quantity,String price,String sector,String supplierId) {
		if(quantity.equalsIgnoreCase("")||price.equalsIgnoreCase("")||sector.equalsIgnoreCase("")||supplierId.equalsIgnoreCase("")) {
			view.Cashier.emptyFields();
		}
		try { if(model.updateInsert.insertNewProduct(productName, Integer.parseInt(quantity), Double.parseDouble(price), Integer.parseInt(sector),Integer.parseInt(supplierId))) {
			view.Cashier.done();
		}
		}
		catch(NumberFormatException n) {
			view.Cashier.productAddError();
		}
	}

}
