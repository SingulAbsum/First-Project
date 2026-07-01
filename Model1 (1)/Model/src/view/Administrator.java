package view;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
public class Administrator {
	public static void menu1(Stage primaryStage) {
	    MenuButton travel = new MenuButton("Go to");
	    travel.setPrefSize(200, 100);
	    travel.setFont(Font.font("Consolas", FontWeight.BOLD, 32));
	    travel.setStyle("-fx-background-color: #4B0082; -fx-text-fill: white; -fx-border-color: #9400D3; -fx-border-width: 2;");
	    MenuItem travel1 = new MenuItem("Login");
	    MenuItem travel2 = new MenuItem("Administrator Login");
	    MenuItem travel3 = new MenuItem("Administrator Terminal");
	    travel.getItems().addAll(travel1, travel2, travel3);
	    Button addNewProductCategory = new Button("Add Cashier");
	    addNewProductCategory.setPrefSize(200, 50);
	    addNewProductCategory.setFont(Font.font("Consolas", FontWeight.MEDIUM, 18));
	    addNewProductCategory.setStyle("-fx-background-color: #32CD32; -fx-text-fill: white; -fx-border-color: #228B22; -fx-border-width: 2;");
	    Button addNewProductCategor = new Button("Add Manager");
	    addNewProductCategor.setPrefSize(200, 50);
	    addNewProductCategor.setFont(Font.font("Consolas", FontWeight.MEDIUM, 18));
	    addNewProductCategor.setStyle("-fx-background-color: #4169E1; -fx-text-fill: white; -fx-border-color: #0000CD; -fx-border-width: 2;");
	    Button addNewProductCatego = new Button("Add Administrator");
	    addNewProductCatego.setPrefSize(200, 50);
	    addNewProductCatego.setFont(Font.font("Consolas", FontWeight.MEDIUM, 18));
	    addNewProductCatego.setStyle("-fx-background-color: #FFD700; -fx-text-fill: black; -fx-border-color: #FF8C00; -fx-border-width: 2;");
	    TextField newProductName = createStyledTextField("Id");
	    TextField productQuantity = createStyledTextField("Name");
	    TextField productPrice = createStyledTextField("Birthdate");
	    TextField productSector = createStyledTextField("Phone Number");
	    TextField supplierId = createStyledTextField("Email");
	    TextField supplierName = createStyledTextField("Salary");
	    TextField supplierCategory = createStyledTextField("Access Level");
	    TextField username = createStyledTextField("Username");
	    TextField password = createStyledTextField("Password");
	    TextField sector = createStyledTextField("Sector");
	    Label npN = createStyledLabel("Id:");
	    Label npQ = createStyledLabel("Name:");
	    Label npP = createStyledLabel("Birthdate:");
	    Label npS = createStyledLabel("Phone Number:");
	    Label sI = createStyledLabel("Email:");
	    Label sN = createStyledLabel("Salary:");
	    Label sC = createStyledLabel("Access Level:");
	    Label uN = createStyledLabel("Username:");
	    Label pW = createStyledLabel("Password:");
	    Label sec = createStyledLabel("Sector:");
	    VBox npName = new VBox(10);
	    npName.getChildren().addAll(
	            npN, newProductName,
	            npQ, productQuantity,
	            npP, productPrice,
	            npS, productSector,
	            sI, supplierId,
	            sN, supplierName,
	            sC, supplierCategory,
	            uN, username,
	            pW, password,
	            sec, sector,
	            addNewProductCategory,
	            addNewProductCategor,
	            addNewProductCatego,
	            travel
	    );
	    ScrollPane scroll=new ScrollPane(npName);
	    scroll.setFitToWidth(true);  
        scroll.setFitToHeight(true); 
	    npName.setAlignment(Pos.CENTER);
	    npName.setStyle("-fx-background-color: #000000; -fx-padding: 20;");
	    Scene newProduct = new Scene(scroll, 1000, 1200);
	    primaryStage.setScene(newProduct);
	    addNewProductCategory.setOnAction(p -> {
	        control.administratorMenu1AddCashierButton.addCashier(
	                newProductName.getText(),
	                productQuantity.getText(),
	                productPrice.getText(),
	                productSector.getText(),
	                supplierId.getText(),
	                supplierName.getText(),
	                supplierCategory.getText(),
	                username.getText(),
	                password.getText(),
	                sector.getText()
	        );
	    });
            addNewProductCategor.setOnAction(p -> {
	        control.administratorMenu1AddManagerButton.addManager(
	                newProductName.getText(),
	                productQuantity.getText(),
	                productPrice.getText(),
	                productSector.getText(),
	                supplierId.getText(),
	                supplierName.getText(),
	                supplierCategory.getText(),
	                username.getText(),
	                password.getText(),
	                sector.getText()
	        );
	    });
	    addNewProductCatego.setOnAction(p -> {
	        control.administratorMenu1AddAdministratorButton.addAdministrator(
	                newProductName.getText(),
	                productQuantity.getText(),
	                productPrice.getText(),
	                productSector.getText(),
	                supplierId.getText(),
	                supplierName.getText(),
	                supplierCategory.getText(),
	                username.getText(),
	                password.getText(),
	                sector.getText()
	        );
	    });
	    travel1.setOnAction(z -> control.cashierBackButton.back(primaryStage));
	    travel2.setOnAction(zh -> control.button3Login.administratorLoginTransition(primaryStage));
	    travel3.setOnAction(zh -> control.administratorLogin.loginTerminal(primaryStage));
	}

	private static TextField createStyledTextField(String promptText) {
	    TextField textField = new TextField();
	    textField.setPromptText(promptText);
	    textField.setPrefSize(300, 40);
	    textField.setFont(Font.font("Consolas", FontWeight.MEDIUM, 14));
	    textField.setStyle("-fx-background-color: #1E1E1E; -fx-text-fill: #00FFFF; -fx-border-color: #00CED1; -fx-border-width: 1;");
	    return textField;
	}

	private static Label createStyledLabel(String text) {
	    Label label = new Label(text);
	    label.setFont(Font.font("Consolas", FontWeight.MEDIUM, 16));
	    label.setStyle("-fx-text-fill: #FFFFFF;");
	    return label;
	}

	public static void menu2(Stage primaryStage) {
		MenuButton travel = new MenuButton("Go to");
		travel.setPrefSize(200, 100);
		travel.setFont(Font.font("Consolas", FontWeight.BOLD, 32));
		travel.setStyle("-fx-background-color: #4B0082; -fx-text-fill: white; -fx-border-color: #9400D3; -fx-border-width: 2;");
		MenuItem travel1 = new MenuItem("Login");
		MenuItem travel2 = new MenuItem("Administrator Login");
		MenuItem travel3 = new MenuItem("Administrator Terminal");
		travel.getItems().addAll(travel1, travel2, travel3);
		ChoiceBox<String> role = new ChoiceBox<>();
		role.getItems().addAll("Cashier", "Manager", "Administrator");
		role.setPrefSize(200, 50);
		role.setStyle("-fx-background-color: #1E1E1E; -fx-text-fill: #00FFFF; -fx-font-size: 16; -fx-border-color: #00CED1; -fx-border-width: 1;");
		ChoiceBox<String> field = new ChoiceBox<>();
		field.getItems().addAll("id", "name", "birthdate", "phone number", "email", "salary", "access level", "username", "password", "sector");
		field.setPrefSize(200, 50);
		field.setStyle("-fx-background-color: #1E1E1E; -fx-text-fill: #00FFFF; -fx-font-size: 16; -fx-border-color: #00CED1; -fx-border-width: 1;");
		TextField newField = new TextField();
		newField.setPromptText("new value");
		newField.setPrefSize(300, 40);
		newField.setStyle("-fx-background-color: #1E1E1E; -fx-text-fill: #00FFFF; -fx-border-color: #00CED1; -fx-border-width: 1; -fx-font-size: 14;");
		Label nF = new Label("New Value:");
		nF.setFont(Font.font("Consolas", FontWeight.MEDIUM, 16));
		nF.setStyle("-fx-text-fill: #FFFFFF;");
		VBox nV = new VBox(5);
		nV.getChildren().addAll(nF, newField);
		nV.setAlignment(javafx.geometry.Pos.CENTER);
		ComboBox<String> name = new ComboBox<>();
		name.setPrefSize(300, 40);
		name.setStyle("-fx-background-color: #1E1E1E; -fx-text-fill: #00FFFF; -fx-border-color: #00CED1; -fx-border-width: 1; -fx-font-size: 14;");
		name.setOnAction(r -> {
			name.getItems().addAll(model.Return.returnName(role.getValue()));
		});
		name.setEditable(true);
		Button edit = new Button("Edit");
		edit.setPrefSize(200, 50);
		edit.setFont(Font.font("Consolas", FontWeight.MEDIUM, 18));
		edit.setStyle("-fx-background-color: #32CD32; -fx-text-fill: white; -fx-border-color: #228B22; -fx-border-width: 2;");
		HBox first = new HBox(25);
		first.getChildren().addAll(role, field);
		first.setAlignment(javafx.geometry.Pos.CENTER);
		HBox second = new HBox(25);
		second.getChildren().addAll(nV, name);
		second.setAlignment(javafx.geometry.Pos.CENTER);
		VBox editing = new VBox(10);
		editing.getChildren().addAll(first, second, edit, travel);
		editing.setAlignment(javafx.geometry.Pos.CENTER);
		editing.setStyle("-fx-background-color: #000000; -fx-padding: 20;");
		Scene editable = new Scene(editing, 1000, 800);
		primaryStage.setScene(editable);
		edit.setOnAction(rr -> {
			control.administratorMenu2EditButton.edit(role.getValue(), field.getValue(), newField.getText(), name.getValue());
		});
		travel1.setOnAction(z -> {
			control.cashierBackButton.back(primaryStage);
		});
		travel2.setOnAction(zh -> {
			control.button3Login.administratorLoginTransition(primaryStage);
		});
		travel3.setOnAction(zh -> {
			control.administratorLogin.loginTerminal(primaryStage);
		});
	}

	public static void menu3(Stage primaryStage) {
		MenuButton travel = new MenuButton("Go to");
		travel.setPrefSize(200, 100);
		travel.setFont(Font.font("Arial Black", FontWeight.BOLD, 32));
		travel.setStyle("-fx-background-color: #3A3A3A; -fx-text-fill: #FFFFFF; -fx-border-color: #FF6347; -fx-border-width: 2;");
		MenuItem travel1 = new MenuItem("Login");
		MenuItem travel2 = new MenuItem("Administrator Login");
		MenuItem travel3 = new MenuItem("Administrator Terminal");
		travel.getItems().addAll(travel1, travel2, travel3);
		ChoiceBox<String> role = new ChoiceBox<>();
		role.getItems().addAll("Cashier", "Manager", "Administrator");
		role.setPrefSize(250, 50);
		role.setStyle("-fx-background-color: #e9edf5; -fx-text-fill: #e9edf5; -fx-font-size: 16; -fx-border-color: #FFD700; -fx-border-width: 1;");
		ComboBox<String> name = new ComboBox<>();
		name.setPrefSize(250, 50);
		name.setStyle("-fx-background-color: #333333; -fx-text-fill: #FFD700; -fx-border-color: #FFD700; -fx-border-width: 1; -fx-font-size: 16;");
		name.setOnAction(r -> {
			name.getItems().addAll(model.Return.returnName(role.getValue()));
		});
		name.setEditable(true);
		Button edit = new Button("Delete");
		edit.setPrefSize(220, 60);
		edit.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
		edit.setStyle("-fx-background-color: #B22222; -fx-text-fill: #FFFFFF; -fx-border-color: #8B0000; -fx-border-width: 2;");
		VBox editing = new VBox(15);
		editing.getChildren().addAll(role, name, edit, travel);
		editing.setAlignment(javafx.geometry.Pos.CENTER);
		editing.setStyle("-fx-background-color: #1C1C1C; -fx-padding: 20; -fx-spacing: 15;");
		Scene editable = new Scene(editing, 1000, 800);
		primaryStage.setScene(editable);
		edit.setOnAction(rr -> {
			control.administratorMenu2DeleteButton.edit(role.getValue(), name.getValue());
		});
		travel1.setOnAction(z -> {
			control.cashierBackButton.back(primaryStage);
		});
		travel2.setOnAction(zh -> {
			control.button3Login.administratorLoginTransition(primaryStage);
		});
		travel3.setOnAction(zh -> {
			control.administratorLogin.loginTerminal(primaryStage);
		});
	}

	public static void menu4(Stage primaryStage) {
		MenuButton travel = new MenuButton("Go to");
		travel.setPrefSize(200, 60);
		travel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		travel.setStyle("-fx-background-color: #37474F; -fx-text-fill: #FFFFFF; -fx-border-color: #03A9F4; -fx-border-width: 2;");
		MenuItem travel1 = new MenuItem("Login");
		MenuItem travel2 = new MenuItem("Administrator Login");
		MenuItem travel3 = new MenuItem("Administrator Terminal");
		travel.getItems().addAll(travel1, travel2, travel3);
		TextField start = new TextField();
		start.setPromptText("Starting Date (Format: MDDYYYY)");
		start.setStyle("-fx-font-size: 14; -fx-padding: 5; -fx-border-color: #03A9F4;");
		start.setPrefSize(300, 40);
		TextField end = new TextField();
		end.setPromptText("Ending Date (Format: MDDYYYY)");
		end.setStyle("-fx-font-size: 14; -fx-padding: 5; -fx-border-color: #03A9F4;");
		end.setPrefSize(300, 40);
		Label s = new Label("Enter Starting Date:");
		s.setFont(Font.font("Arial", FontWeight.BOLD, 16));
		s.setStyle("-fx-text-fill: #FFFFFF;");
		Label e = new Label("Enter Ending Date:");
		e.setFont(Font.font("Arial", FontWeight.BOLD, 16));
		e.setStyle("-fx-text-fill: #FFFFFF;");
		VBox startS = new VBox(10, s, start);
		startS.setAlignment(javafx.geometry.Pos.CENTER);
		VBox endE = new VBox(10, e, end);
		endE.setAlignment(javafx.geometry.Pos.CENTER);
		Button generate = new Button("Generate Report");
		generate.setPrefSize(250, 60);
		generate.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
		generate.setStyle("-fx-background-color: #03A9F4; -fx-text-fill: #FFFFFF; -fx-border-color: #0288D1; -fx-border-width: 2;");
		VBox gen = new VBox(30, startS, endE, generate, travel);
		gen.setAlignment(javafx.geometry.Pos.CENTER);
		gen.setStyle("-fx-background-color: #263238; -fx-padding: 40;");
		Scene stats = new Scene(gen, 1000, 800);
		primaryStage.setScene(stats);
		generate.setOnAction(t -> {
			control.administratorMenu4StatisticalReport.stats(primaryStage, start.getText(), end.getText());
		});
		travel1.setOnAction(z -> {
			control.cashierBackButton.back(primaryStage);
		});
		travel2.setOnAction(zh -> {
			control.button3Login.administratorLoginTransition(primaryStage);
		});
		travel3.setOnAction(zh -> {
			control.administratorLogin.loginTerminal(primaryStage);
		});
	}

	public static void statisticsList(Stage primaryStage, String start, String end) {
		MenuButton travel = new MenuButton("Go to");
		travel.setPrefSize(200, 60);
		travel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		travel.setStyle("-fx-background-color: #37474F; -fx-text-fill: #FFFFFF; -fx-border-color: #03A9F4; -fx-border-width: 2;");
		MenuItem travel1 = new MenuItem("Login");
		MenuItem travel2 = new MenuItem("Administrator Login");
		MenuItem travel3 = new MenuItem("Administrator Terminal");
		MenuItem travel4 = new MenuItem("Generate Report");
		travel.getItems().addAll(travel1, travel2, travel3, travel4);
		Button itemsSoldRevenue = new Button("Total Revenue: " + model.Return.returnItemsSoldRevenueTimed(start, end));
		itemsSoldRevenue.setPrefSize(300, 50);
		itemsSoldRevenue.setFont(Font.font("Arial", FontWeight.BOLD, 16));
		itemsSoldRevenue.setStyle("-fx-background-color: #388E3C; -fx-text-fill: #FFFFFF; -fx-border-color: #2E7D32; -fx-border-width: 2;");
		Button itemsPurchasedRevenue = new Button("Total Expense: " + model.Return.returnFinalTotalExpense(start, end));
		itemsPurchasedRevenue.setPrefSize(300, 50);
		itemsPurchasedRevenue.setFont(Font.font("Arial", FontWeight.BOLD, 16));
		itemsPurchasedRevenue.setStyle("-fx-background-color: #D32F2F; -fx-text-fill: #FFFFFF; -fx-border-color: #C62828; -fx-border-width: 2;");
		Button bilance = new Button("Balance: " + (model.Return.returnItemsSoldRevenue() - model.Return.returnFinalTotalExpense(start, end)));
		bilance.setPrefSize(300, 50);
		bilance.setFont(Font.font("Arial", FontWeight.BOLD, 16));
		bilance.setStyle("-fx-background-color: #FBC02D; -fx-text-fill: #000000; -fx-border-color: #F9A825; -fx-border-width: 2;");
		Label sr = new Label("Revenue from Items Sold:");
		sr.setFont(Font.font("Arial", FontWeight.BOLD, 16));
		sr.setStyle("-fx-text-fill: #FFFFFF;");
		Label pr = new Label("Expense from Items Purchased:");
		pr.setFont(Font.font("Arial", FontWeight.BOLD, 16));
		pr.setStyle("-fx-text-fill: #FFFFFF;");
		Label b = new Label("Overall Balance:");
		b.setFont(Font.font("Arial", FontWeight.BOLD, 16));
		b.setStyle("-fx-text-fill: #FFFFFF;");
		VBox rs = new VBox(10, sr, itemsSoldRevenue);
		rs.setAlignment(javafx.geometry.Pos.CENTER);
		VBox rp = new VBox(10, pr, itemsPurchasedRevenue);
		rp.setAlignment(javafx.geometry.Pos.CENTER);
		VBox bb = new VBox(10, b, bilance);
		bb.setAlignment(javafx.geometry.Pos.CENTER);
		HBox results = new HBox(30, rs, rp, bb, travel);
		results.setAlignment(javafx.geometry.Pos.CENTER);
		results.setStyle("-fx-background-color: #263238; -fx-padding: 40;");
		Scene show = new Scene(results, 1000, 800);
		primaryStage.setScene(show);
		travel1.setOnAction(z -> {
			control.cashierBackButton.back(primaryStage);
		});
		travel2.setOnAction(zh -> {
			control.button3Login.administratorLoginTransition(primaryStage);
		});
		travel3.setOnAction(zh -> {
			control.administratorLogin.loginTerminal(primaryStage);
		});
		travel4.setOnAction(zh -> {
			control.Administrator.menu4(primaryStage);
		});
	}

}
