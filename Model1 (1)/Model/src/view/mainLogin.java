package view;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;   

public class mainLogin extends Application {
	static Scene home;
	@Override
	public void start(Stage primaryStage) {
	    primaryStage.setTitle("Login");
	    primaryStage.setFullScreen(true);
	    HBox layout = new HBox(50);
	    VBox lay = new VBox(350);
	    home = new Scene(lay, 1000, 800);
	    home.setFill(javafx.scene.paint.Color.web("#2c3e50")); 
	    primaryStage.setScene(home);
	    Button button1 = new Button("Cashier");
	    Button button2 = new Button("Manager");
	    Button button3 = new Button("Administrator");
	    button1.setPrefSize(200, 100);
	    button2.setPrefSize(200, 100);
	    button3.setPrefSize(200, 100);
	    button1.setFont(new Font(24));
	    button2.setFont(new Font(24));
	    button3.setFont(new Font(24));
	    button1.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-background-radius: 10; -fx-border-color: #2980b9; -fx-border-width: 2;");
	    button2.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-background-radius: 10; -fx-border-color: #1e8449; -fx-border-width: 2;");
	    button3.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-background-radius: 10; -fx-border-color: #c0392b; -fx-border-width: 2;");
	    
	    button1.setOnMouseEntered(e -> button1.setStyle("-fx-background-color: #5dade2; -fx-text-fill: white; -fx-background-radius: 10; -fx-border-color: #2980b9; -fx-border-width: 2;"));
	    button1.setOnMouseExited(e -> button1.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-background-radius: 10; -fx-border-color: #2980b9; -fx-border-width: 2;"));

	    button2.setOnMouseEntered(e -> button2.setStyle("-fx-background-color: #52be80; -fx-text-fill: white; -fx-background-radius: 10; -fx-border-color: #1e8449; -fx-border-width: 2;"));
	    button2.setOnMouseExited(e -> button2.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-background-radius: 10; -fx-border-color: #1e8449; -fx-border-width: 2;"));

	    button3.setOnMouseEntered(e -> button3.setStyle("-fx-background-color: #f1948a; -fx-text-fill: white; -fx-background-radius: 10; -fx-border-color: #c0392b; -fx-border-width: 2;"));
	    button3.setOnMouseExited(e -> button3.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-background-radius: 10; -fx-border-color: #c0392b; -fx-border-width: 2;"));

	    Text text = new Text("User Login");
	    text.setTranslateY(50);
	    text.setFont(new Font(48));
	    text.setFill(javafx.scene.paint.Color.web("#bbde42")); 

	    layout.getChildren().addAll(button3, button2, button1);
	    layout.setAlignment(javafx.geometry.Pos.CENTER);

	    lay.getChildren().addAll(text, layout);
	    lay.setAlignment(javafx.geometry.Pos.TOP_CENTER);

	    button1.setOnAction(e -> {
	        control.button1Login.cashierLoginTransition(primaryStage);
	    });

	    button2.setOnAction(e -> {
	        control.button2Login.managerLoginTransition(primaryStage);
	    });

	    button3.setOnAction(e -> {
	        control.button3Login.administratorLoginTransition(primaryStage);
	    });

	    primaryStage.show();
	}

	public static void administratorLogin(Stage primaryStage) {
	    VBox cashier=new VBox(100);
	    VBox user=new VBox(5);
	    VBox pass = new VBox(5);
	    Label user1 = new Label("Username");
	    user1.setStyle("-fx-text-fill: white");
	    Label pass1 = new Label("Password");
	    pass1.setStyle("-fx-text-fill: white");
	    TextField username = new TextField();
	    TextField password = new TextField();
	    username.setPromptText("Username"); 
	    username.setPrefSize(10, 50); 
	    user1.setTranslateX(500); 
	    VBox.setMargin(username, new Insets(5, 500, 5, 500));
	    password.setPromptText("Password"); 
	    password.setPrefSize(10, 50); 
	    pass1.setTranslateX(500); 
	    VBox.setMargin(password, new Insets(5, 500, 5, 500));
	    user.getChildren().addAll(user1, username); 
	    pass.getChildren().addAll(pass1, password);
	    Text authenticate = new Text("Administrator Login"); 
	    authenticate.setFont(new Font(30));
	    authenticate.setFill(javafx.scene.paint.Color.web("#ecf0f1"));
	    Button login = new Button("Login");
	    Button back = new Button("Back");
	    login.setPrefSize(100, 50); 
	    login.setFont(new Font(18)); 
	    login.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-background-radius: 15; -fx-border-color: #229954; -fx-border-width: 2;");
	    back.setPrefSize(100, 50); back.setFont(new Font(18)); back.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-background-radius: 15; -fx-border-color: #c0392b; -fx-border-width: 2;");
	    login.setOnMouseEntered(e -> login.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-background-radius: 15; -fx-border-color: #229954; -fx-border-width: 2;"));
	    login.setOnMouseExited(e -> login.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-background-radius: 15; -fx-border-color: #229954; -fx-border-width: 2;"));
	    back.setOnMouseEntered(e -> back.setStyle("-fx-background-color: #f1948a; -fx-text-fill: white; -fx-background-radius: 15; -fx-border-color: #c0392b; -fx-border-width: 2;"));
	    back.setOnMouseExited(e -> back.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-background-radius: 15; -fx-border-color: #c0392b; -fx-border-width: 2;"));
	    cashier.setAlignment(javafx.geometry.Pos.CENTER); cashier.setStyle("-fx-background-color: #2c3e50;");
	    cashier.getChildren().addAll(authenticate, user, pass, login, back);
	    control.button3Login.setScene(new Scene(cashier, 1000, 800), primaryStage);   
	    back.setOnAction(a -> control.cashierBackButton.back(primaryStage));
	    login.setOnAction(f -> control.administratorLogin.login(primaryStage, username.getText(), password.getText()));
	}

	public static void managerLogin(Stage primaryStage) {
		VBox cashier = new VBox(100);
		VBox user = new VBox(5);
		Label user1 = new Label("Username");
		user1.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2C3E50;");
		TextField username = new TextField();
		username.setPromptText("Enter your username");
		username.setPrefSize(300, 50);
		username.setStyle("-fx-font-size: 16px; -fx-border-color: #3498DB; -fx-border-radius: 10px; -fx-background-radius: 10px;");
		user1.setTranslateX(500);
		VBox.setMargin(username, new Insets(5, 500, 5, 500));
		user.getChildren().addAll(user1, username);
		TextField password = new TextField();
		password.setPromptText("Enter your password");
		password.setPrefSize(300, 50);
		password.setStyle("-fx-font-size: 16px; -fx-border-color: #E74C3C; -fx-border-radius: 10px; -fx-background-radius: 10px;");
		VBox pass = new VBox(5);
		Label pass1 = new Label("Password");
		pass1.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2C3E50;");
		pass1.setTranslateX(500);
		VBox.setMargin(password, new Insets(5, 500, 5, 500));
		pass.getChildren().addAll(pass1, password);
		Text authenticate = new Text("Manager Login");
		authenticate.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-fill: #8E44AD;");
		Button login = new Button("Login");
		login.setPrefSize(150, 50);
		login.setFont(new Font(18));
		login.setStyle("-fx-background-color: #27AE60; -fx-text-fill: white; -fx-background-radius: 10px;");
		Button back = new Button("Back");
		back.setPrefSize(150, 50);
		back.setFont(new Font(18));
		back.setStyle("-fx-background-color: #C0392B; -fx-text-fill: white; -fx-background-radius: 10px;");
		cashier.setAlignment(javafx.geometry.Pos.CENTER);
		cashier.setStyle("-fx-background-color: #F2F3F4;");
		cashier.getChildren().addAll(authenticate, user, pass, login, back);
		primaryStage.setScene(new Scene(cashier, 1000, 800));
		primaryStage.setFullScreen(true);
		back.setOnAction(a -> {
			control.cashierBackButton.back(primaryStage);
		});
		login.setOnAction(f -> {
			control.managerLogin.login(primaryStage, username.getText(), password.getText());
		});
	}

	
	public static void cashierLogin(Stage primaryStage) {
		VBox cashier = new VBox(100);
		VBox user = new VBox(5);
		Label user1 = new Label("Username");
		user1.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #34495E;");
		TextField username = new TextField();
		username.setPromptText("Enter your username");
		username.setPrefSize(300, 50);
		username.setStyle("-fx-font-size: 16px; -fx-border-color: #2980B9; -fx-border-radius: 10px; -fx-background-radius: 10px; -fx-effect: dropshadow(gaussian, #2980B9, 10, 0, 2, 2);");
		user1.setTranslateX(500);
		VBox.setMargin(username, new Insets(5, 500, 5, 500));
		user.getChildren().addAll(user1, username);
		TextField password = new TextField();
		password.setPromptText("Enter your password");
		password.setPrefSize(300, 50);
		password.setStyle("-fx-font-size: 16px; -fx-border-color: #E67E22; -fx-border-radius: 10px; -fx-background-radius: 10px; -fx-effect: dropshadow(gaussian, #E67E22, 10, 0, 2, 2);");
		VBox pass = new VBox(5);
		Label pass1 = new Label("Password");
		pass1.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #34495E;");
		pass1.setTranslateX(500);
		VBox.setMargin(password, new Insets(5, 500, 5, 500));
		pass.getChildren().addAll(pass1, password);
		Text authenticate = new Text("Cashier Login");
		authenticate.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-fill: linear-gradient(to right, #8E44AD, #3498DB); -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 10, 0, 2, 2);");
		Button login = new Button("Login");
		login.setPrefSize(150, 50);
		login.setFont(new Font(18));
		login.setStyle("-fx-background-color: #27AE60; -fx-text-fill: white; -fx-background-radius: 10px; -fx-effect: dropshadow(gaussian, #27AE60, 10, 0, 2, 2);");
		Button back = new Button("Back");
		back.setPrefSize(150, 50);
		back.setFont(new Font(18));
		back.setStyle("-fx-background-color: #C0392B; -fx-text-fill: white; -fx-background-radius: 10px; -fx-effect: dropshadow(gaussian, #C0392B, 10, 0, 2, 2);");
		cashier.setAlignment(javafx.geometry.Pos.CENTER);
		cashier.setStyle("-fx-background-color: linear-gradient(to bottom, #ECF0F1, #BDC3C7);");
		cashier.getChildren().addAll(authenticate, user, pass, login, back);
		primaryStage.setScene(new Scene(cashier, 1000, 800));
		primaryStage.setFullScreen(true);
		back.setOnAction(a -> {
			control.cashierBackButton.back(primaryStage);
		});
		login.setOnAction(f -> {
			control.cashierLogin.login(primaryStage, username.getText(), password.getText());
		});
	}

	public static void home(Stage primaryStage) {
		primaryStage.setScene(home);
		primaryStage.setFullScreen(true);
	}
	public static void cashierLoginTerminal(Stage primaryStage) {
		MenuButton context = new MenuButton("Cashier");
		context.setPrefSize(200, 100);
		context.setFont(new Font("Arial Bold", 32));
		context.setStyle("-fx-background-color: #1E90FF; -fx-text-fill: white; -fx-border-color: #4682B4; -fx-border-radius: 10px; -fx-background-radius: 10px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.6), 10, 0, 2, 2);");
		MenuButton travel = new MenuButton("Go to");
		travel.setPrefSize(200, 100);
		travel.setFont(new Font("Arial Bold", 32));
		travel.setAlignment(javafx.geometry.Pos.TOP_LEFT);
		travel.setStyle("-fx-background-color: #32CD32; -fx-text-fill: white; -fx-border-color: #2E8B57; -fx-border-radius: 10px; -fx-background-radius: 10px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.6), 10, 0, 2, 2);");
		MenuItem travel1 = new MenuItem("Login");
		MenuItem travel2 = new MenuItem("Cashier Login");
		travel1.setStyle("-fx-font-size: 20px;");
		travel2.setStyle("-fx-font-size: 20px;");
		travel.getItems().addAll(travel1, travel2);
		MenuItem menu1 = new MenuItem("Create new bill.");
		MenuItem menu2 = new MenuItem("View bill statistics.");
		menu1.setStyle("-fx-font-size: 20px;");
		menu2.setStyle("-fx-font-size: 20px;");
		context.getItems().addAll(menu1, menu2);
		VBox border = new VBox(50);
		border.getChildren().addAll(context, travel);
		border.setAlignment(javafx.geometry.Pos.CENTER);
		border.setStyle("-fx-padding: 50; -fx-background-color: linear-gradient(to bottom, #DCDCDC, #B0C4DE); -fx-border-color: #696969; -fx-border-width: 2px;");
		ImageView electronicsImage = new ImageView(new Image("https://example.com/electronics-store-bg.jpg"));
		electronicsImage.setFitWidth(1000);
		electronicsImage.setFitHeight(800);
		electronicsImage.setOpacity(0.2);
		StackPane root = new StackPane();
		root.getChildren().addAll(electronicsImage, border);
		Scene menu = new Scene(root, 1000, 800);
		primaryStage.setScene(menu);
		menu1.setOnAction(a -> {
			control.menu1.menu1(primaryStage);
		});
		menu2.setOnAction(g -> {
			control.menu2Transition.transition(primaryStage);
		});
		travel1.setOnAction(z -> {
			control.cashierBackButton.back(primaryStage);
		});
		travel2.setOnAction(zh -> {
			control.button1Login.cashierLoginTransition(primaryStage);
		});
	}

	public static void managerLoginTerminal(Stage primaryStage) {
        MenuButton travel = new MenuButton("Go to");
		travel.setPrefSize(200, 100);
		travel.setFont(new Font(32));
		travel.setStyle("-fx-background-color: #3498DB; -fx-text-fill: white; -fx-background-radius: 10px; -fx-effect: dropshadow(gaussian, #3498DB, 10, 0, 2, 2);");
		MenuItem travel1 = new MenuItem("Login");
		MenuItem travel2 = new MenuItem("Manager Login");
		travel.getItems().addAll(travel1, travel2);
        MenuButton context = new MenuButton("Manager");
		context.setPrefSize(300, 100);
		context.setFont(new Font(32));
		context.setStyle("-fx-background-color: #2ECC71; -fx-text-fill: white; -fx-background-radius: 10px; -fx-effect: dropshadow(gaussian, #2ECC71, 10, 0, 2, 2);");
		MenuItem menu1 = new MenuItem("Add new product category");
		MenuItem menu3 = new MenuItem("Add new product");
		MenuItem menu2 = new MenuItem("View bill statistics");
		MenuItem menu4 = new MenuItem("Notifications");
		menu1.setStyle("-fx-font-size: 18px; -fx-text-fill: #34495E;");
		menu3.setStyle("-fx-font-size: 18px; -fx-text-fill: #34495E;");
		menu2.setStyle("-fx-font-size: 18px; -fx-text-fill: #34495E;");
		menu4.setStyle("-fx-font-size: 18px; -fx-text-fill: #34495E;");
		context.getItems().addAll(menu1, menu3, menu2, menu4);
        VBox border = new VBox(50);
		border.getChildren().addAll(context, travel);
		border.setAlignment(javafx.geometry.Pos.CENTER);
		border.setStyle("-fx-background-color: rgba(255,255,255,0.8); -fx-padding: 20px; -fx-border-color: #34495E; -fx-border-width: 5px; -fx-border-radius: 15px;");
        Scene menu = new Scene(border, 1000, 800);
		primaryStage.setScene(menu);
        menu3.setOnAction(h -> {
			control.managerMenu3.managerMenu3(primaryStage);
		});
		menu1.setOnAction(j -> {
			control.menagerMenu1.menagerMenu1(primaryStage);
		});
		menu4.setOnAction(l -> {
			control.managerMenu4.managerMenu4(primaryStage);
		});
		menu2.setOnAction(m -> {
			control.managerMenu2.managerMenu2(primaryStage);
		});
		travel1.setOnAction(z -> {
			control.cashierBackButton.back(primaryStage);
		});
		travel2.setOnAction(zh -> {
			control.button2Login.managerLoginTransition(primaryStage);
		});
	}

	public static void administratorLoginTerminal(Stage primaryStage) {
		MenuButton travel = new MenuButton("Go to");
		travel.setPrefSize(200, 100);
		travel.setFont(new Font(32));
		travel.setStyle("-fx-background-color: linear-gradient(to right, #6a11cb, #2575fc); -fx-text-fill: white; -fx-background-radius: 20px; -fx-border-color: #34495E; -fx-border-radius: 20px; -fx-effect: dropshadow(gaussian, #6a11cb, 10, 0.3, 2, 2);");
		MenuItem travel1 = new MenuItem("Login");
		MenuItem travel2 = new MenuItem("Administrator Login");
		travel.getItems().addAll(travel1, travel2);
        MenuButton context = new MenuButton("Administrator");
		context.setPrefSize(300, 100);
		context.setFont(new Font(32));
		context.setStyle("-fx-background-color: linear-gradient(to right, #0f2027, #203a43, #2c5364); -fx-text-fill: white; -fx-background-radius: 20px; -fx-border-color: #34495E; -fx-border-radius: 20px; -fx-effect: dropshadow(gaussian, #203a43, 10, 0.3, 2, 2);");
		MenuItem menu1 = new MenuItem("Add employee");
		MenuItem menu2 = new MenuItem("Edit employee");
		MenuItem menu3 = new MenuItem("Delete employee");
		MenuItem menu4 = new MenuItem("View Statistics");
		menu1.setStyle("-fx-font-size: 18px; -fx-text-fill: #E0E0E0;");
		menu2.setStyle("-fx-font-size: 18px; -fx-text-fill: #E0E0E0;");
		menu3.setStyle("-fx-font-size: 18px; -fx-text-fill: #E0E0E0;");
		menu4.setStyle("-fx-font-size: 18px; -fx-text-fill: #E0E0E0;");
		context.getItems().addAll(menu1, menu2, menu3, menu4);
        VBox border = new VBox(50);
		border.getChildren().addAll(context, travel);
		border.setAlignment(javafx.geometry.Pos.CENTER);
		border.setStyle("-fx-background-color: rgba(40, 44, 52, 0.9); -fx-padding: 20px; -fx-border-color: #1f4068; -fx-border-width: 5px; -fx-border-radius: 20px;");
		Glow glow = new Glow(0.5);
		Animation glowAnimation = new Timeline(
			new KeyFrame(Duration.ZERO, new KeyValue(glow.levelProperty(), 0.5)),
			new KeyFrame(Duration.seconds(1.5), new KeyValue(glow.levelProperty(), 1.0)),
			new KeyFrame(Duration.seconds(3), new KeyValue(glow.levelProperty(), 0.5))
		);
		glowAnimation.setCycleCount(Animation.INDEFINITE);
		glowAnimation.setAutoReverse(true);
		border.setEffect(glow);
        StackPane root = new StackPane();
		root.getChildren().add(border);
		root.setStyle("-fx-background-color: linear-gradient(to bottom, #141E30, #243B55);");
        Scene menu = new Scene(root, 1000, 800);
		primaryStage.setScene(menu);
        menu1.setOnAction(o -> {
			control.Administrator.menu1(primaryStage);
		});
		menu2.setOnAction(q -> {
			control.Administrator.menu2(primaryStage);
		});
		menu3.setOnAction(s -> {
			control.Administrator.menu3(primaryStage);
		});
		menu4.setOnAction(sh -> {
			control.Administrator.menu4(primaryStage);
		});
		travel1.setOnAction(z -> {
			control.cashierBackButton.back(primaryStage);
		});
		travel2.setOnAction(zh -> {
			control.button3Login.administratorLoginTransition(primaryStage);
		});
	}

	public static void loginAlert() {
	    Alert alert = new Alert(Alert.AlertType.ERROR);
	    alert.setTitle("Login Error");
	    alert.setHeaderText("Authentication Fail");
	    alert.setContentText("Invalid Username or Password!");
	    DialogPane dialogPane = alert.getDialogPane();
	    dialogPane.setStyle("-fx-background-color: #2c3e50; -fx-border-color: #e74c3c; -fx-border-width: 2;");
	    dialogPane.lookup(".header-panel").setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10;");
	    dialogPane.lookup(".content").setStyle("-fx-text-fill: #ecf0f1; -fx-font-size: 14px; -fx-padding: 10;");
	    alert.getDialogPane().lookupButton(ButtonType.OK).setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-background-radius: 5; -fx-border-color: #c0392b; -fx-border-width: 2;");
	    alert.showAndWait();
	}

}
