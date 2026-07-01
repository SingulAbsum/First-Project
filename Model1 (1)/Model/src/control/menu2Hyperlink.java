package control;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

public class menu2Hyperlink {
public static void hyperlink(String DIRECTORY,String fileName) {
	try {
		File file=new File(DIRECTORY+"/"+fileName);	
		if(file.exists()) {
			Desktop.getDesktop().open(file);
		}
		else {
			view.Cashier.hyperlinkError(fileName);
		}
	}
	catch(IOException ex) {
		ex.printStackTrace();
	}
}
}
