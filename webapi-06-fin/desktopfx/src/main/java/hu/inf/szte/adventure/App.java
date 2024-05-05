package hu.inf.szte.adventure;

import hu.inf.szte.adventure.util.fx.SceneLoader;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    private final SceneLoader sceneLoader = new SceneLoader(App.class, null);

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        Scene scene = new Scene(sceneLoader.loadFXML("main"));
        stage.setScene(scene);
        stage.show();
    }
}
