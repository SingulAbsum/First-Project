package control;
import javafx.scene.control.Label;
import java.util.*;
import javafx.collections.ObservableList;
public class menu1AddButton {
public static void add(String productName,String quantity,String sector,Label total,HashMap<String,Integer> register,ArrayList<Integer> sec,ObservableList<String> base) {
	if(productName.equalsIgnoreCase("")||quantity.equalsIgnoreCase("")||sector.equalsIgnoreCase("")) {
		view.Cashier.emptyFields();
	}
	try { if(model.Database.checkProduct(productName, Integer.parseInt(quantity), Integer.parseInt(sector))) {
		double productPrice=model.Return.returnProductPrice(productName);
		String product=productName+" "+Integer.parseInt(quantity)+"x"+productPrice+" "+"Total:"+Integer.parseInt(quantity)*productPrice;
		double tot=model.Return.getTotal()+Integer.parseInt(quantity)*productPrice;
		model.Return.setTotal(tot);
		 total.setText(String.format("Total: %.2f", tot));
		 register.put(productName, Integer.parseInt(quantity));
		 sec.add(Integer.parseInt(sector));
		 base.addAll(product);
	}
	}
	catch(NumberFormatException n) {
		view.Cashier.productAddError();
	}
}
}
