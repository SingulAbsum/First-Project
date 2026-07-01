package control;
import javafx.animation.FadeTransition;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;
public class button3Login {
public static void administratorLoginTransition(Stage primaryStage) {
	view.mainLogin.administratorLogin(primaryStage);
}
public static void setScene(Scene scene,Stage primaryStage) {
	FadeTransition fadeOut = new FadeTransition(Duration.seconds(1),scene.getRoot());
    fadeOut.setFromValue(1);
    fadeOut.setToValue(0);
    fadeOut.setOnFinished(e -> {
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), scene.getRoot());
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
    });
    fadeOut.play();
}
}
