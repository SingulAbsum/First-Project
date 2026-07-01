package control;
import javafx.stage.Stage;
public class managerLogin {
	public static void login(Stage primaryStage,String username,String password) {
		if(model.Database.checkCredentialsManager(username, password)) {
			view.mainLogin.managerLoginTerminal(primaryStage);
		}
		else {
			view.mainLogin.loginAlert();
		}
	}
	public static void loginTerminal(Stage primaryStage) {
			view.mainLogin.managerLoginTerminal(primaryStage);
	}
}
