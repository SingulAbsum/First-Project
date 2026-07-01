package view;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.*;

public class Cashier {
	public static void menu1(Stage primaryStage) {
		ComboBox<String> productName = new ComboBox<>();
		productName.setPromptText("Product Name");
		productName.setEditable(true);
		ObservableList<String> names = model.Return.returnAllProductNames();
		productName.setItems(names);
		productName.setStyle("-fx-background-color: linear-gradient(to right, #283048, #859398); -fx-text-fill: white; -fx-border-color: #34495E; -fx-border-radius: 10px; -fx-background-radius: 10px; -fx-font-size: 16px; -fx-prompt-text-fill: gray;");
		productName.getEditor().textProperty().addListener((list, oldValue, newValue) -> {
		if (newValue == null || newValue.isEmpty()) {
		return;
		}
		FXCollections.sort(names, (a, b) -> {
		if (a.startsWith(newValue) && !b.startsWith(newValue)) {
		return -1;
		} else if (!a.startsWith(newValue) && b.startsWith(newValue)) {
		return 1;
		} else {
		return a.compareTo(b);
		}
		});
		});
		VBox pName = new VBox(5);
		Label pn = new Label("Product Name");
		pn.setStyle("-fx-font-size: 18px; -fx-text-fill: #E0E0E0; -fx-font-family: 'Arial';");
		pName.getChildren().addAll(pn, productName);
		pName.setTranslateY(50);
		TextField productSector = new TextField();
		productSector.setPromptText("Product Sector");
		productSector.setStyle("-fx-background-color: #2c3e50; -fx-text-fill: white; -fx-border-color: #34495E; -fx-border-radius: 10px; -fx-background-radius: 10px; -fx-font-size: 16px; -fx-prompt-text-fill: gray;");
		VBox pSector = new VBox(5);
		pSector.setTranslateY(50);
		Label ps = new Label("Product Sector");
		ps.setStyle("-fx-font-size: 18px; -fx-text-fill: #E0E0E0; -fx-font-family: 'Arial';");
		pSector.getChildren().addAll(ps, productSector);
		TextField quantity = new TextField();
		quantity.setPromptText("Quantity");
		VBox pQuantity = new VBox(5);
		pQuantity.setTranslateY(50);
		Label pq = new Label("Product Quantity");
		pq.setStyle("-fx-font-size: 18px; -fx-text-fill: #E0E0E0; -fx-font-family: 'Arial';");
		HBox info = new HBox(50);
		Button add = new Button("Add");
		add.setTranslateY(5);
		add.setStyle("-fx-background-color: #34495E; -fx-text-fill: white; -fx-font-size: 18px; -fx-border-radius: 10px; -fx-background-radius: 10px;");
		MenuButton travel = new MenuButton("Go to");
		travel.setPrefSize(200, 100);
		travel.setFont(new Font(32));
		MenuItem travel1 = new MenuItem("Login");
		MenuItem travel2 = new MenuItem("Cashier Login");
		MenuItem travel3 = new MenuItem("Cashier Terminal");
		travel.getItems().addAll(travel1, travel2, travel3);
		pQuantity.getChildren().addAll(pq, quantity, add);
		info.getChildren().addAll(pName, pQuantity, pSector, travel);
		ObservableList<String> base = FXCollections.observableArrayList();
		ListView<String> list = new ListView<>(base);
		VBox listing = new VBox(50);
		Button finish = new Button("Finish");
		finish.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-size: 18px; -fx-border-radius: 10px; -fx-background-radius: 10px;");
		Label total = new Label("Total:" + model.Return.getTotal());
		total.setStyle("-fx-font-size: 20px; -fx-text-fill: #E0E0E0; -fx-font-family: 'Arial';");
		listing.getChildren().addAll(info, list, finish, total);
		Scene create = new Scene(listing, 1000, 800);
		primaryStage.setScene(create);
		HashMap<String, Integer> register = new HashMap<>();
		ArrayList<Integer> sec = new ArrayList<>();
		add.setOnAction(b -> {
		control.menu1AddButton.add(productName.getValue(), quantity.getText(), productSector.getText(), total, register, sec, base);
		productName.getEditor().clear();
		quantity.clear();
		productSector.clear();
		});
		finish.setOnAction(c -> {
		control.menu1FinishButton.finish(register, sec, list, total.getText());
		});
		travel1.setOnAction(z -> {
		control.cashierBackButton.back(primaryStage);
		});
		travel2.setOnAction(zh -> {
		control.button1Login.cashierLoginTransition(primaryStage);
		});
		travel3.setOnAction(zh -> {
		control.cashierLogin.loginTerminal(primaryStage);
		});
		}

	public static void emptyFields() {
		Alert ale = new Alert(Alert.AlertType.ERROR);
		ale.setTitle("Quantity Error");
		ale.setHeaderText("⚠ Quantity Add Failure ⚠");
		ale.setContentText("🚫 Empty Quantity or Sector! 🚫\nPlease ensure all fields are filled.");
		ale.getDialogPane().setStyle("-fx-background-color: linear-gradient(to right, #adc2c7, #9fa1dd); -fx-border-color: #E74C3C; -fx-border-radius: 10px; -fx-background-radius: 10px; -fx-font-family: 'Verdana'; -fx-font-size: 14px; -fx-text-fill: white;");
		ale.showAndWait();
		}

	public static void productAddError() {
	    Alert alert = new Alert(Alert.AlertType.ERROR);
	    alert.setTitle("\u26A1 Product Error"); 
	    alert.setHeaderText("\uD83D\uDEE0 Product Add Failure"); 
	    alert.setContentText("\u274C Invalid Product Name or Quantity! \nPlease verify and try again."); 
	    DialogPane dialogPane = alert.getDialogPane();
	    dialogPane.setStyle("-fx-background-color: linear-gradient(to bottom, #8c6971, #d0e57b); -fx-font-family: 'Consolas'; -fx-font-size: 14px; -fx-text-fill: white");
	    alert.showAndWait();
	}

	public static void productQuantityUpdateError() {
		Alert al=new Alert(Alert.AlertType.ERROR);
		al.setTitle("\u26A1 Update Failure"); 
		al.setContentText("\u274C Review Database! \nEnsure the database is accessible and data is valid."); 
		DialogPane dialogPane = al.getDialogPane();
		dialogPane.setStyle("-fx-background-color: linear-gradient(to bottom, #2A2A2A, #121212); -fx-font-family: 'Consolas'; -fx-font-size: 14px; -fx-text-fill: white;");
		al.showAndWait();
		}

	public static void done() {
		Alert al = new Alert(Alert.AlertType.INFORMATION);
		al.setTitle("\u2705 Insert Success"); 
		al.setHeaderText("\uD83C\uDF10 Inserted"); 
		al.setContentText("\uD83D\uDCB3 Bill Added! \nYour bill has been successfully recorded."); 
		DialogPane dialogPane = al.getDialogPane();
		dialogPane.setStyle("-fx-background-color: linear-gradient(to bottom, #E8F5E9, #C8E6C9); -fx-font-family: 'Consolas'; -fx-font-size: 14px; -fx-text-fill: #2E7D32;");
		al.showAndWait();
		}


	public static void menu2(Stage primaryStage) {
		MenuButton travel = new MenuButton("\uD83D\uDEAA Go to"); 
		travel.setPrefSize(200, 100);
		travel.setFont(new Font(32));
		MenuItem travel1 = new MenuItem("\uD83D\uDD10 Login"); 
		MenuItem travel2 = new MenuItem("\uD83D\uDC68\u200D\uD83D\uDCBB Cashier Login"); 
		MenuItem travel3 = new MenuItem("\uD83D\uDED2 Cashier Terminal"); 
		travel.getItems().addAll(travel1, travel2, travel3);
		ListView<Hyperlink> list = new ListView<>(control.menu2.menu2());
		VBox layou = new VBox(50);
		Label tota = new Label("\uD83D\uDCB0 Total: " + model.Get.getTotalBills()); 
		layou.getChildren().addAll(travel, list, tota);
		layou.setAlignment(javafx.geometry.Pos.CENTER);
		layou.setStyle("-fx-background-color: linear-gradient(to bottom, #1A237E, #0D47A1); -fx-font-family: 'Consolas'; -fx-font-size: 14px; -fx-text-fill: white;");
		Scene fileListScene = new Scene(layou, 1000, 800);
		primaryStage.setScene(fileListScene);
		travel1.setOnAction(z -> {
		control.cashierBackButton.back(primaryStage);
		});
		travel2.setOnAction(zh -> {
		control.button1Login.cashierLoginTransition(primaryStage);
		});
		travel3.setOnAction(zh -> {
		control.cashierLogin.loginTerminal(primaryStage);
		});
		}


	public static void hyperlinkError(String fileName) {
		 Alert alert = new Alert(Alert.AlertType.ERROR);
         alert.setTitle("File Error");
         alert.setHeaderText("File Not Found");
         alert.setContentText("The file " + fileName + " does not exist.");
         alert.showAndWait();
	}
	public static void noBillsAlert() {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("\u274C No Files Found");
		alert.setHeaderText("\uD83D\uDCC2 No Bills Available"); 
		alert.setContentText("\uD83D\uDD10 No bill files are available in the database. \nPlease check again later."); 
		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.setStyle("-fx-background-color: linear-gradient(to bottom, #F44336, #B71C1C); -fx-font-family: 'Consolas'; -fx-font-size: 14px; -fx-text-fill: white;");
		alert.showAndWait();
		return;
		}


}



