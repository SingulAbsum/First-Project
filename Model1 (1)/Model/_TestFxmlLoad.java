import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import javafx.fxml.FXMLLoader;

public class TestFxmlLoad {
    public static void main(String[] args) throws Exception {
        Path p = Paths.get("").toAbsolutePath().resolve("src/main/resources/view/cashier/bill-editor.fxml");
        System.out.println("path=" + p);
        URL url = p.toUri().toURL();
        System.out.println("url=" + url);
        try {
            new FXMLLoader(url).load();
            System.out.println("LOAD_OK");
        } catch (Exception ex) {
            System.out.println("LOAD_FAIL: " + ex.getClass().getName() + ": " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
