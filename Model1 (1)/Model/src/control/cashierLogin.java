package control;
import javafx.stage.Stage;
public class cashierLogin {
	public static void login(Stage primaryStage,String username,String password) {
		if(model.Database.checkCredentialsCashier(username, password)) {
			model.updateInsert.insertNewCashierActivity(username);
			view.mainLogin.cashierLoginTerminal(primaryStage);
			
		}
		else {
			view.mainLogin.loginAlert();
		}
	}
	public static void loginTerminal(Stage primaryStage) {
			view.mainLogin.cashierLoginTerminal(primaryStage);
	}

}
