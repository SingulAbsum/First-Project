package control;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Hyperlink;
public class menu2 {
public static ObservableList<Hyperlink> menu2() {
	 final String DIRECTORY = "C:/Users/User/eclipse-workspace/Model";
	 ObservableList<String> base=model.Get.getFileNames();
	 ObservableList<Hyperlink> hyperlinkList = FXCollections.observableArrayList();
	 for(String fileName:base) {
		 Hyperlink hyperlink=new Hyperlink(fileName);
		 hyperlink.setOnAction(h->{
			 control.menu2Hyperlink.hyperlink(DIRECTORY,fileName);
		 });
		 hyperlinkList.add(hyperlink);
	 }
	 if (hyperlinkList.isEmpty()) {
		 view.Cashier.noBillsAlert();
	 }
	 return hyperlinkList;
}
}
