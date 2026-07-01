package control;
import javafx.stage.Stage;
public class administratorLogin {
public static void login(Stage primaryStage,String username,String password) {
	if(model.Database.checkCredentialsAdministrator(username, password)) {
		view.mainLogin.administratorLoginTerminal(primaryStage);
	}
	else {
		view.mainLogin.loginAlert();
	}
}
public static void loginTerminal(Stage primaryStage) {
		view.mainLogin.administratorLoginTerminal(primaryStage);
}
}
