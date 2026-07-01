package control;
import javafx.stage.Stage;
public class managerMenu2CashierButtonGenerateButton {
public static void generate(Stage primaryStage,String name,String start,String end) {
	if(name.equalsIgnoreCase("")||start.equalsIgnoreCase("")||end.equalsIgnoreCase("")) {
		view.Cashier.emptyFields();
	}
	else {
		view.Manager.cashierList(primaryStage,name,start,end);
	}
}

}
