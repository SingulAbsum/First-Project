package control;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javafx.scene.control.ListView;
public class menu1FinishButton {
public static void finish(HashMap<String,Integer> register,ArrayList<Integer> sec,ListView<String> list,String total) {
	int i=0;
	int totalQuantity=0;
	for(Map.Entry<String,Integer> z:register.entrySet()) {
		if(model.updateInsert.updateProductQuantity(z.getKey(),z.getValue(),sec.get(i))==0) {
			view.Cashier.productQuantityUpdateError();
			break;
		}
		i++;
		totalQuantity+=z.getValue();
		}
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddyyyy");
	String formattedDate = LocalDateTime.now().format(formatter);
	File file=new File((model.Return.returnBillNr()+1)+formattedDate+".txt");
	 try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
         for (String item : list.getItems()) {
             writer.write(item);
             writer.newLine();
         }
         writer.write(total);
     } 
	 catch (IOException d) {
         d.printStackTrace();
     }
	 int nr=model.Return.returnBillNr()+1;
     String name=nr+formattedDate+".txt";
     if( model.updateInsert.insertBill(nr, formattedDate, name, model.Return.getTotal(),totalQuantity)) {
     view.Cashier.done();
     }
}
}
