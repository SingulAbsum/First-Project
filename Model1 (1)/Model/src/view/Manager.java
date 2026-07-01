package view;
import java.awt.Graphics2D;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
public class Manager {
	public static void menu3(Stage primaryStage) {
		MenuButton travel = new MenuButton("\uD83D\uDEAA Go to"); 
		travel.setPrefSize(200, 100);
		travel.setFont(new Font(32));
		MenuItem travel1 = new MenuItem("\uD83D\uDD10 Login"); 
		MenuItem travel2 = new MenuItem("\uD83D\uDC68\u200D\uD83D\uDCBB Manager Login"); 
		MenuItem travel3 = new MenuItem("\uD83D\uDED2 Manager Terminal"); 
		travel.getItems().addAll(travel1, travel2, travel3);
		Button addNewProduct = new Button("\uD83D\uDE80 Add Product");
		addNewProduct.setPrefSize(200, 50);
		addNewProduct.setFont(new Font(18));
		TextField newProductName = new TextField();
		newProductName.setPromptText("\uD83C\uDF10 Product Name"); 
		TextField productQuantity = new TextField();
		productQuantity.setPromptText("\uD83D\uDCCA Product Quantity"); 
		TextField productPrice = new TextField();
		productPrice.setPromptText("\uD83D\uDCB3 Product Price"); 
		TextField productSector = new TextField();
		productSector.setPromptText("\uD83D\uDEE0 Product Sector"); 
		TextField supplierId = new TextField();
		supplierId.setPromptText("\uD83D\uDD0E Supplier Id"); 
		VBox npName = new VBox(5);
		Label npN = new Label("\uD83C\uDF10 Product Name");
		npN.setTextFill(Paint.valueOf("#df5b93"));
		Label npQ = new Label("\uD83D\uDCCA Product Quantity");
		npQ.setTextFill(Paint.valueOf("#df5b93"));
		Label npP = new Label("\uD83D\uDCB3 Product Price");
		npP.setTextFill(Paint.valueOf("#df5b93"));
		Label npS = new Label("\uD83D\uDEE0 Product Sector");
		npS.setTextFill(Paint.valueOf("#df5b93"));
		Label npSI = new Label("\uD83D\uDD0E Supplier Id");
		npSI.setTextFill(Paint.valueOf("#df5b93"));
		npName.getChildren().addAll(npN, newProductName, npQ, productQuantity, npP, productPrice, npS, productSector, npSI, supplierId, addNewProduct, travel);
		npName.setTranslateY(50);
		npName.setStyle("-fx-background-color: linear-gradient(to bottom, #1A237E, #0D47A1); -fx-font-family: 'Consolas'; -fx-font-size: 14px; -fx-text-fill:pink;");
		Scene newProduct = new Scene(npName, 1000, 800);
		primaryStage.setScene(newProduct);
		addNewProduct.setOnAction(i -> {
		control.managerMenu3AddNewProduct.addNewProduct(newProductName.getText(), productQuantity.getText(), productPrice.getText(), productSector.getText(), supplierId.getText());
		});
		travel1.setOnAction(z -> {
		control.cashierBackButton.back(primaryStage);
		});
		travel2.setOnAction(zh -> {
		control.button2Login.managerLoginTransition(primaryStage);
		});
		travel3.setOnAction(zh -> {
		control.managerLogin.loginTerminal(primaryStage);
		});
		}


	public static void menu1(Stage primaryStage) {
	    MenuButton travel = new MenuButton("Go to");
	    travel.setPrefSize(200, 100);
	    travel.setFont(Font.font("Digital-7", 32));
	    MenuItem travel1 = new MenuItem("Login");
	    MenuItem travel2 = new MenuItem("Manager Login");
	    MenuItem travel3 = new MenuItem("Manager Terminal");
	    travel.getItems().addAll(travel1, travel2, travel3);
	    Button addNewProductCategory = new Button("Add Product Category");
	    addNewProductCategory.setPrefSize(200, 50);
	    addNewProductCategory.setFont(Font.font("Digital-7", 18)); 
	    TextField newProductName = new TextField();
	    newProductName.setPromptText("Product Name");
	    newProductName.setStyle("-fx-background-color: #333; -fx-text-fill: #00ff00;");  
	    TextField productQuantity = new TextField();
	    productQuantity.setPromptText("Product Quantity");
	    productQuantity.setStyle("-fx-background-color: #333; -fx-text-fill: #00ff00;");	    
	    TextField productPrice = new TextField();
	    productPrice.setPromptText("Product Price");
	    productPrice.setStyle("-fx-background-color: #333; -fx-text-fill: #00ff00;");	    
	    TextField productSector = new TextField();
	    productSector.setPromptText("Product Sector");
	    productSector.setStyle("-fx-background-color: #333; -fx-text-fill: #00ff00;");    
	    TextField supplierId = new TextField();
	    supplierId.setPromptText("Supplier Id");
	    supplierId.setStyle("-fx-background-color: #333; -fx-text-fill: #00ff00;");    
	    TextField supplierName = new TextField();
	    supplierName.setPromptText("Supplier Name");
	    supplierName.setStyle("-fx-background-color: #333; -fx-text-fill: #00ff00;");	    
	    TextField supplierCategory = new TextField();
	    supplierCategory.setPromptText("Supplier Category");
	    supplierCategory.setStyle("-fx-background-color: #333; -fx-text-fill: #00ff00;");
	    VBox npName = new VBox(5);
	    Label npN = new Label("Product Name");
	    Label npQ = new Label("Product Quantity");
	    Label npP = new Label("Product Price");
	    Label npS = new Label("Product Sector");
	    Label sI = new Label("Supplier Id");
	    Label sN = new Label("Supplier Name");
	    Label sC = new Label("Supplier Category");
	    npName.getChildren().addAll(npN, newProductName, npQ, productQuantity, npP, productPrice, npS, productSector, sI, supplierId, sN, supplierName, sC, supplierCategory, addNewProductCategory, travel);
	    npName.setStyle("-fx-background-color: #783e20; -fx-padding: 20px; -fx-border-radius: 15px;");
	    npName.setTranslateY(50);
	    Scene newProduct = new Scene(npName, 1000, 800);
	    newProduct.setFill(Color.web("#1a1a1a")); 
	    primaryStage.setScene(newProduct);
	    addNewProductCategory.setOnAction(i -> {
	        control.managerMenu1AddNewProductCategory.addNewProductCategory(
	            newProductName.getText(), productQuantity.getText(), productPrice.getText(), 
	            productSector.getText(), supplierId.getText(), supplierName.getText(), supplierCategory.getText()
	        );
	    });
	    travel1.setOnAction(z -> {
	        control.cashierBackButton.back(primaryStage);   
	    });
	    travel2.setOnAction(zh -> {
	        control.button2Login.managerLoginTransition(primaryStage);    
	    });
	    travel3.setOnAction(zh -> {
	        control.managerLogin.loginTerminal(primaryStage);    
	    });
	}

	public static void menu4(Stage primaryStage, int sectorNr) {
	    ObservableList<String> base = model.Get.getNotifications(sectorNr);
	    ListView<String> list = new ListView<>();
	    list.setItems(base);
	    list.setStyle("-fx-background-color: #1a1a1a; -fx-text-fill: #00ff00; -fx-font-family: 'Digital-7';"); 
	    list.setPrefHeight(1000);
	    list.setPrefWidth(800);
	    list.setCellFactory(m -> new ListCell<String>() {
	        @Override
	        protected void updateItem(String name, boolean state) {
	            super.updateItem(name, state);
	            if (state || name == null) {
	                setGraphic(null);
	            } else {
	                TextField quantity = new TextField();
	                quantity.setPromptText("Quantity");
	                quantity.setPrefSize(50, 50);
	                quantity.setStyle("-fx-background-color: #333; -fx-text-fill: #00ff00; -fx-border-color: #00ff00;");  
	                Label q = new Label("Quantity");
	                q.setStyle("-fx-text-fill: #00ff00;");                
	                Label qt = new Label(name);
	                qt.setStyle("-fx-text-fill: #00ff00; -fx-font-family: 'Digital-7';");                  
	                VBox qq = new VBox(5);
	                qq.getChildren().addAll(q, quantity);
	                VBox ss = new VBox(5);
	                Button refill = new Button("Refill");
	                refill.setPrefSize(100, 50);
	                refill.setFont(new Font("Digital-7", 18));
	                refill.setStyle("-fx-background-color: #00ff00; -fx-text-fill: #1a1a1a; -fx-border-radius: 10px;");  	                
	                HBox h = new HBox(20);
	                h.getChildren().addAll(qt, qq, ss, refill);
	                setGraphic(h);
	                setPrefWidth(2000);
	                setPrefHeight(200);
	                h.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
	                refill.setOnAction(ll -> {
	                    control.managerMenu4RefillButton.refill(name, quantity.getText(), String.valueOf(sectorNr));
	                    list.setItems(model.Get.getNotifications(sectorNr));
	                });
	            }
	        }
	    });
	    Pane con = new Pane(list);
	    VBox cont = new VBox(10);
	    MenuButton travel = new MenuButton("Go to");
	    travel.setPrefSize(200, 100);
	    travel.setFont(Font.font("Digital-7", 32));
	    travel.setStyle("-fx-background-color: #00ff00; -fx-border-color: #00ff00;");	    
	    MenuItem travel1 = new MenuItem("Login");
	    MenuItem travel2 = new MenuItem("Manager Login");
	    MenuItem travel3 = new MenuItem("Manager Terminal");
	    travel.getItems().addAll(travel1, travel2, travel3);	    
	    cont.getChildren().addAll(con, travel);
	    Scene notify = new Scene(cont, 1000, 800);
	    notify.setFill(Color.web("#111")); 
	    primaryStage.setScene(notify);
	    travel1.setOnAction(z -> {
	        control.cashierBackButton.back(primaryStage);
	    });
	    travel2.setOnAction(zh -> {
	        control.button2Login.managerLoginTransition(primaryStage);
	    });
	    travel3.setOnAction(zh -> {
	        control.managerLogin.loginTerminal(primaryStage);
	    });
	}

	public static void menu2(Stage primaryStage) {
	    MenuButton travel = new MenuButton("Go to");
	    travel.setPrefSize(200, 100);
	    travel.setFont(Font.font("Arial", FontWeight.BOLD, 32));
	    travel.setStyle("-fx-background-color: #1E90FF; -fx-text-fill: white; -fx-border-color: #00CED1; -fx-border-width: 2;");
	    MenuItem travel1 = new MenuItem("Login");
	    MenuItem travel2 = new MenuItem("Manager Login");
	    MenuItem travel3 = new MenuItem("Manager Terminal");
	    travel.getItems().addAll(travel1, travel2, travel3);
	    Button cashier = new Button("Cashier Statistics");
	    cashier.setPrefSize(200, 100);
	    cashier.setFont(Font.font("Arial", FontWeight.MEDIUM, 18));
	    cashier.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-border-color: #008000; -fx-border-width: 2;");
	    Button general = new Button("General Statistics");
	    general.setPrefSize(200, 100);
	    general.setFont(Font.font("Arial", FontWeight.MEDIUM, 18));
	    general.setStyle("-fx-background-color: #FF6347; -fx-text-fill: white; -fx-border-color: #B22222; -fx-border-width: 2;");
	    HBox statistics = new HBox(25);
	    statistics.getChildren().addAll(cashier, general, travel);
	    statistics.setAlignment(javafx.geometry.Pos.CENTER);
	    statistics.setStyle("-fx-background-color: #2F4F4F;"); 
	    Scene statistical = new Scene(statistics, 1000, 800);
	    primaryStage.setScene(statistical);
	    cashier.setOnAction(n -> {
	        control.managerMenu2CashierButton.cashier(primaryStage);
	    });
	    general.setOnAction(o -> {
	        control.managerMenu2GeneralButton.generalButton(primaryStage);
	    });
	    travel1.setOnAction(z -> {
	        control.cashierBackButton.back(primaryStage);
	    });
	    travel2.setOnAction(zh -> {
	        control.button2Login.managerLoginTransition(primaryStage);
	    });
	    travel3.setOnAction(zh -> {
	        control.managerLogin.loginTerminal(primaryStage);
	    });
	}

	public static void managerCashierButton(Stage primaryStage) {
	    MenuButton travel = new MenuButton("Go to");
	    travel.setPrefSize(200, 100);
	    travel.setFont(Font.font("Consolas", FontWeight.BOLD, 32));
	    travel.setStyle("-fx-background-color: #4B0082; -fx-text-fill: white; -fx-border-color: #9400D3; -fx-border-width: 2;");
	    MenuItem travel1 = new MenuItem("Login");
	    MenuItem travel2 = new MenuItem("Manager Login");
	    MenuItem travel3 = new MenuItem("Manager Terminal");
	    MenuItem travel4 = new MenuItem("Manager Report Selection");
	    travel.getItems().addAll(travel1, travel2, travel3, travel4);
	    TextField cashierName = new TextField();
	    cashierName.setPromptText("Cashier Name");
	    cashierName.setPrefSize(200, 50);
	    cashierName.setStyle("-fx-font-size: 16px; -fx-background-color: #1E1E1E; -fx-text-fill: #00FFFF; -fx-border-color:#00FFFF; ");
	    Label cn = new Label("Cashier Name");
	    cn.setStyle("-fx-font-size: 18px; -fx-text-fill: #FFFFFF;");
	    TextField start = new TextField();
	    start.setPromptText("Starting Date");
	    start.setPrefSize(200, 50);
	    start.setStyle("-fx-font-size: 16px; -fx-background-color: #1E1E1E; -fx-text-fill: #00FFFF;-fx-border-color:#00FFFF;");
	    Label s = new Label("Starting Date Format: MDDYYYY (no dots)");
	    s.setStyle("-fx-font-size: 18px; -fx-text-fill: #FFFFFF;");
	    TextField end = new TextField();
	    end.setPromptText("Ending Date");
	    end.setPrefSize(200, 50);
	    end.setStyle("-fx-font-size: 16px; -fx-background-color: #1E1E1E; -fx-text-fill: #00FFFF;-fx-border-color:#00FFFF;");
	    Label e = new Label("Ending Date Format: MDDYYYY (no dots)");
	    e.setStyle("-fx-font-size: 18px; -fx-text-fill: #FFFFFF;");
	    VBox cashierN = new VBox(5);
	    cashierN.getChildren().addAll(cn, cashierName);
	    VBox startS = new VBox(5);
	    startS.getChildren().addAll(s, start);
	    VBox endE = new VBox(5);
	    endE.getChildren().addAll(e, end);
	    Button generate = new Button("Generate");
	    generate.setPrefSize(200, 100);
	    generate.setFont(Font.font("Consolas", FontWeight.MEDIUM, 18));
	    generate.setStyle("-fx-background-color: #228B22; -fx-text-fill: white; -fx-border-color: #006400; -fx-border-width: 2;");
	    VBox gen = new VBox(20);
	    gen.getChildren().addAll(cashierN, startS, endE, generate, travel);
	    gen.setStyle("-fx-background-color: #000000; -fx-padding: 20;");
	    gen.setAlignment(Pos.CENTER);
	    Scene stats = new Scene(gen, 1000, 800);
	    primaryStage.setScene(stats);
	    generate.setOnAction(nj -> {
	        control.managerMenu2CashierButtonGenerateButton.generate(primaryStage, cashierName.getText(), start.getText(), end.getText());
	    });
	    travel1.setOnAction(z -> {
	        control.cashierBackButton.back(primaryStage);
	    });
	    travel2.setOnAction(zh -> {
	        control.button2Login.managerLoginTransition(primaryStage);
	    });
	    travel3.setOnAction(zh -> {
	        control.managerLogin.loginTerminal(primaryStage);
	    });
	    travel4.setOnAction(zh -> {
	        control.managerMenu2.managerMenu2(primaryStage);
	    });
	}

	public static void cashierList(Stage primaryStage, String name, String start, String end) {
	    MenuButton travel = new MenuButton("Go to");
	    travel.setPrefSize(200, 100);
	    travel.setFont(Font.font("Consolas", FontWeight.BOLD, 32));
	    travel.setStyle("-fx-background-color: #4B0082; -fx-text-fill: white; -fx-border-color: #9400D3; -fx-border-width: 2;");
	    MenuItem travel1 = new MenuItem("Login");
	    MenuItem travel2 = new MenuItem("Manager Login");
	    MenuItem travel3 = new MenuItem("Manager Terminal");
	    MenuItem travel4 = new MenuItem("Manager Report Selection");
	    MenuItem travel5 = new MenuItem("Generate Report");
	    travel.getItems().addAll(travel1, travel2, travel3, travel4, travel5);
	    Label c = new Label("Total Nr Of Bills Generated");
	    c.setStyle("-fx-font-size: 18px; -fx-text-fill: #FFFFFF;");
	    Label cc = new Label("Total Items Sold");
	    cc.setStyle("-fx-font-size: 18px; -fx-text-fill: #FFFFFF;");
	    Label ccc = new Label("Total Revenue");
	    ccc.setStyle("-fx-font-size: 18px; -fx-text-fill: #FFFFFF;");
	    Button totalNrOfBills = new Button(String.valueOf(model.Return.returnTotalBills(name, start, end)));
	    totalNrOfBills.setPrefSize(200, 100);
	    totalNrOfBills.setFont(Font.font("Consolas", FontWeight.MEDIUM, 18));
	    totalNrOfBills.setStyle("-fx-background-color: #4169E1; -fx-text-fill: white; -fx-border-color: #0000CD; -fx-border-width: 2;");
	    Button totalItemsSold = new Button(String.valueOf(model.Return.returnTotalItems(name, start, end)));
	    totalItemsSold.setPrefSize(200, 100);
	    totalItemsSold.setFont(Font.font("Consolas", FontWeight.MEDIUM, 18));
	    totalItemsSold.setStyle("-fx-background-color: #32CD32; -fx-text-fill: white; -fx-border-color: #228B22; -fx-border-width: 2;");
	    Button totalRevenue = new Button(String.valueOf(model.Return.returnTotalRevenue(name, start, end)));
	    totalRevenue.setPrefSize(200, 100);
	    totalRevenue.setFont(Font.font("Consolas", FontWeight.MEDIUM, 18));
	    totalRevenue.setStyle("-fx-background-color: #FFD700; -fx-text-fill: black; -fx-border-color: #FF8C00; -fx-border-width: 2;");
	    HBox results = new HBox(25);
	    VBox c1=new VBox(5);
	    c1.getChildren().addAll(c,totalNrOfBills);
	    VBox c2=new VBox();
	    c2.getChildren().addAll(cc,totalItemsSold);
	    VBox c3=new VBox(5);
	    c3.getChildren().addAll(ccc,totalRevenue);
	    results.getChildren().addAll(c1, c2, c3, travel);
	    results.setAlignment(javafx.geometry.Pos.CENTER);
	    results.setStyle("-fx-background-color: #000000;");
	    Scene show = new Scene(results, 1000, 800);
	    primaryStage.setScene(show);
	    travel1.setOnAction(z -> {
	        control.cashierBackButton.back(primaryStage);
	    });
	    travel2.setOnAction(zh -> {
	        control.button2Login.managerLoginTransition(primaryStage);
	    });
	    travel3.setOnAction(zh -> {
	        control.managerLogin.loginTerminal(primaryStage);
	    });
	    travel4.setOnAction(zh -> {
	        control.managerMenu2.managerMenu2(primaryStage);
	    });
	    travel5.setOnAction(zh -> {
	        control.managerMenu2CashierButton.cashier(primaryStage);
	    });
	}

	public static void managerGeneralButton(Stage primaryStage) {
	    MenuButton travel = new MenuButton("Go to");
	    travel.setPrefSize(200, 100);
	    travel.setFont(Font.font("Consolas", FontWeight.BOLD, 32));
	    travel.setStyle("-fx-background-color: #4B0082; -fx-text-fill: white; -fx-border-color: #9400D3; -fx-border-width: 2;");
	    MenuItem travel1 = new MenuItem("Login");
	    MenuItem travel2 = new MenuItem("Manager Login");
	    MenuItem travel3 = new MenuItem("Manager Terminal");
	    MenuItem travel4 = new MenuItem("Manager Report Selection");
	    travel.getItems().addAll(travel1, travel2, travel3, travel4);
	    TextField start = new TextField();
	    start.setPromptText("Starting Date");
	    start.setPrefSize(200, 50);
	    start.setStyle("-fx-font-size: 16px; -fx-background-color: #1E1E1E; -fx-text-fill: #00FFFF;-fx-border-color:#aee016;");
	    Label s = new Label("Starting Date Format: MDDYYYY (no dots)");
	    s.setStyle("-fx-font-size: 18px; -fx-text-fill: #FFFFFF;");
	    TextField end = new TextField();
	    end.setPromptText("Ending Date");
	    end.setPrefSize(200, 50);
	    end.setStyle("-fx-font-size: 16px; -fx-background-color: #1E1E1E; -fx-text-fill: #00FFFF;-fx-border-color:#aee016;");
	    Label e = new Label("Ending Date Format: MDDYYYY (no dots)");
	    e.setStyle("-fx-font-size: 18px; -fx-text-fill: #FFFFFF;");
	    VBox startS = new VBox(5);
	    startS.getChildren().addAll(s, start);
	    VBox endE = new VBox(5);
	    endE.getChildren().addAll(e, end);
	    Button generate = new Button("Generate");
	    generate.setPrefSize(200, 100);
	    generate.setFont(Font.font("Consolas", FontWeight.MEDIUM, 18));
	    generate.setStyle("-fx-background-color: #228B22; -fx-text-fill: white; -fx-border-color: #006400; -fx-border-width: 2;");
	    VBox gen = new VBox(20);
	    gen.getChildren().addAll(startS, endE, generate, travel);
	    gen.setAlignment(Pos.CENTER);
	    gen.setStyle("-fx-background-color: #000000; -fx-padding: 20;");
	    Scene stats = new Scene(gen, 1000, 800);
	    primaryStage.setScene(stats);
	    generate.setOnAction(nj -> {
	        control.managerMenu2GeneralButtonStatisticalReport.generalStats(primaryStage, start.getText(), end.getText());
	    });
	    travel1.setOnAction(z -> {
	        control.cashierBackButton.back(primaryStage);
	    });
	    travel2.setOnAction(zh -> {
	        control.button2Login.managerLoginTransition(primaryStage);
	    });
	    travel3.setOnAction(zh -> {
	        control.managerLogin.loginTerminal(primaryStage);
	    });
	    travel4.setOnAction(zh -> {
	        control.managerMenu2.managerMenu2(primaryStage);
	    });
	}

	public static void generalList(Stage primaryStage, String start, String end) {
	    MenuButton travel = new MenuButton("Go to");
	    travel.setPrefSize(200, 100);
	    travel.setFont(Font.font("Consolas", FontWeight.BOLD, 32));
	    travel.setStyle("-fx-background-color: #4B0082; -fx-text-fill: white; -fx-border-color: #9400D3; -fx-border-width: 2;");
	    MenuItem travel1 = new MenuItem("Login");
	    MenuItem travel2 = new MenuItem("Manager Login");
	    MenuItem travel3 = new MenuItem("Manager Terminal");
	    MenuItem travel4 = new MenuItem("Manager Report Selection");
	    MenuItem travel5 = new MenuItem("Generate Report");
	    travel.getItems().addAll(travel1, travel2, travel3, travel4, travel5);
	    Label c = new Label("Total Nr Of Items Sold");
	    c.setStyle("-fx-font-size: 18px; -fx-text-fill: #FFFFFF;");
	    Label cc = new Label("Total Nr Of Items Purchased");
	    cc.setStyle("-fx-font-size: 18px; -fx-text-fill: #FFFFFF;");
	    Label ccc = new Label("Items Sold Revenue");
	    ccc.setStyle("-fx-font-size: 18px; -fx-text-fill: #FFFFFF;");
	    Label cccc = new Label("Items Purchased Revenue");
	    cccc.setStyle("-fx-font-size: 18px; -fx-text-fill: #FFFFFF;");
	    Label ccccc = new Label("Bilance");
	    ccccc.setStyle("-fx-font-size: 18px; -fx-text-fill: #FFFFFF;");
	    Button totalNrOfItemsSold = new Button(String.valueOf(model.Return.returnItemsSold()));
	    totalNrOfItemsSold.setPrefSize(200, 100);
	    totalNrOfItemsSold.setFont(Font.font("Consolas", FontWeight.MEDIUM, 18));
	    totalNrOfItemsSold.setStyle("-fx-background-color: #32CD32; -fx-text-fill: white; -fx-border-color: #228B22; -fx-border-width: 2;");
	    Button totalNrOfItemsPurchased = new Button(String.valueOf(model.Return.returnItemsPurchased()));
	    totalNrOfItemsPurchased.setPrefSize(200, 100);
	    totalNrOfItemsPurchased.setFont(Font.font("Consolas", FontWeight.MEDIUM, 18));
	    totalNrOfItemsPurchased.setStyle("-fx-background-color: #4169E1; -fx-text-fill: white; -fx-border-color: #0000CD; -fx-border-width: 2;");
	    Button itemsSoldRevenue = new Button(String.valueOf(model.Return.returnItemsSoldRevenue()));
	    itemsSoldRevenue.setPrefSize(200, 100);
	    itemsSoldRevenue.setFont(Font.font("Consolas", FontWeight.MEDIUM, 18));
	    itemsSoldRevenue.setStyle("-fx-background-color: #FFD700; -fx-text-fill: black; -fx-border-color: #FF8C00; -fx-border-width: 2;");
	    Button itemsPurchasedRevenue = new Button(String.valueOf(model.Return.returnItemsPurchasedRevenue()));
	    itemsPurchasedRevenue.setPrefSize(200, 100);
	    itemsPurchasedRevenue.setFont(Font.font("Consolas", FontWeight.MEDIUM, 18));
	    itemsPurchasedRevenue.setStyle("-fx-background-color: #8A2BE2; -fx-text-fill: white; -fx-border-color: #4B0082; -fx-border-width: 2;");
	    Button bilance = new Button(String.valueOf(model.Return.returnItemsSoldRevenue() - model.Return.returnItemsPurchasedRevenue()));
	    bilance.setPrefSize(200, 100);
	    bilance.setFont(Font.font("Consolas", FontWeight.MEDIUM, 18));
	    bilance.setStyle("-fx-background-color: #FF4500; -fx-text-fill: white; -fx-border-color: #B22222; -fx-border-width: 2;");
	    VBox si = new VBox(5);
	    si.getChildren().addAll(c, totalNrOfItemsSold);
	    VBox pi = new VBox(5);
	    pi.getChildren().addAll(cc, totalNrOfItemsPurchased);
	    VBox rs = new VBox(5);
	    rs.getChildren().addAll(ccc, itemsSoldRevenue);
	    VBox rp = new VBox(5);
	    rp.getChildren().addAll(cccc, itemsPurchasedRevenue);
	    VBox bb = new VBox(5);
	    bb.getChildren().addAll(ccccc, bilance);
	    HBox results = new HBox(25);
	    results.getChildren().addAll(si, pi, rs, rp, bb, travel);
	    results.setAlignment(Pos.CENTER);
	    results.setStyle("-fx-background-color: #000000; -fx-padding: 20;");
	    Scene show = new Scene(results, 1000, 800);
	    primaryStage.setScene(show);
	    travel1.setOnAction(z -> {
	        control.cashierBackButton.back(primaryStage);
	    });
	    travel2.setOnAction(zh -> {
	        control.button2Login.managerLoginTransition(primaryStage);
	    });
	    travel3.setOnAction(zh -> {
	        control.managerLogin.loginTerminal(primaryStage);
	    });
	    travel4.setOnAction(zh -> {
	        control.managerMenu2.managerMenu2(primaryStage);
	    });
	    travel5.setOnAction(zh -> {
	        control.managerMenu2GeneralButton.generalButton(primaryStage);
	    });
	}
}