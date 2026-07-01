package control;

public class managerMenu1AddNewProductCategory {
	public static void addNewProductCategory(String productName,String quantity,String price,String sector,String supplierId,String supplierName,String supplierCategory) {
		if(quantity.equalsIgnoreCase("")||price.equalsIgnoreCase("")||sector.equalsIgnoreCase("")||supplierId.equalsIgnoreCase("")) {
			view.Cashier.emptyFields();
		}
		try { if(model.updateInsert.insertNewProductCategory(productName, Integer.parseInt(quantity), Double.parseDouble(price), Integer.parseInt(sector),Integer.parseInt(supplierId),supplierName,supplierCategory)) {
			view.Cashier.done();
		}
		}
		catch(NumberFormatException n) {
			view.Cashier.productAddError();
		}
	}

}
