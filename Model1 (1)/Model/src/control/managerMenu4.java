package control;
import javafx.stage.Stage;
import javafx.stage.Popup;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
public class managerMenu4 {
public static void managerMenu4(Stage primaryStage) {
	Popup popup = new Popup();
	TextField sectorNr=new TextField();
	Button ok=new Button("ok");
	ok.setTranslateX(50);
popup.getContent().addAll(sectorNr,ok);
ok.setOnAction(lll->{
	try {
	control.managerMenu4.managerMenu4Final(primaryStage, Integer.parseInt(sectorNr.getText()));
	popup.hide();
	}
	catch(NumberFormatException n) {
		view.Cashier.productAddError();
	}
});
popup.show(primaryStage);
}
public static void managerMenu4Final(Stage primaryStage,int sectorNr) {
	view.Manager.menu4(primaryStage,sectorNr);
}
}
