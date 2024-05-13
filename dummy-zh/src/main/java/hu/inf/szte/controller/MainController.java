package hu.inf.szte.controller;

import hu.inf.szte.App;
import hu.inf.szte.util.fx.SceneLoader;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    private SceneLoader sceneLoader;

    @FXML
    private AnchorPane rootPane;

    public MainController() {
    }

    @FXML
    private void initialize() {
        sceneLoader = new SceneLoader(App.class, rootPane);
        sceneLoader.changePanel("dummy/read");
    }

    @FXML
    private void onCloseMenuItem() {
        var alertResult = new Alert(Alert.AlertType.CONFIRMATION,
                "Sure you want to close the application?",
                ButtonType.YES, ButtonType.NO, ButtonType.CANCEL)
                .showAndWait();
        if (alertResult.isPresent() && alertResult.get() == ButtonType.YES) {
            Platform.exit();
        }
    }

    @FXML
    private void onDummyListMenuItem() {
        sceneLoader.changePanel("dummy/read");
    }

    @FXML
    private void onDummyAddMenuItem() {
        var stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);

        try {
            stage.setScene(new Scene(sceneLoader.loadFXML("dummy/add")));
            stage.showAndWait();
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR,
                    "Failed to load scene.\n%s".formatted(e.getMessage()),
                    ButtonType.OK)
                    .showAndWait();
        }
    }

    @FXML
    private void onAboutMenuItem() {
        new Alert(Alert.AlertType.INFORMATION,
                "This app is made with great care, just for you, and only you <3",
                ButtonType.OK)
                .showAndWait();
    }
}
