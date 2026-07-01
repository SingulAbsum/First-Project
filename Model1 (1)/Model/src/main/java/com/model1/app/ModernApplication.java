package main.java.com.model1.app;

import javafx.application.Application;
import javafx.stage.Stage;

public class ModernApplication extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Model1 Command Center");
        primaryStage.setMinWidth(1100);
        primaryStage.setMinHeight(720);

        SceneRouter router = new SceneRouter(primaryStage);
        AppContext.getInstance().setSceneRouter(router);
        router.showLogin();

        primaryStage.show();
    }
}
