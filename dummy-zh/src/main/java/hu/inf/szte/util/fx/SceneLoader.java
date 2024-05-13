package hu.inf.szte.util.fx;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

import java.io.IOException;

/**
 * Utility for fxml file loading,
 * and changing contents of a given panel.
 *
 * @param resource The class that provides context for resource loading
 * @param pane Optional, can be any type of {@link Pane}
 *             (e.g. {@link javafx.scene.layout.AnchorPane}, {@link javafx.scene.layout.VBox}, etc...)
 */
public record SceneLoader(Class<?> resource, Pane pane) {

    private static final String VIEW_ROOT = "fxml";

    /**
     * Changes the panel of the `Pane` object, given at instantiation.
     *
     * @param parent The parent holding the contents to be loaded inside the `Pane` object
     * @throws NullPointerException Throws if pane is not given at construction
     */
    public void changePanel(Parent parent) throws NullPointerException {
        pane.getChildren().clear();
        pane.getChildren().add(parent);
    }

    /**
     * See {@link SceneLoader#changePanel(Parent)}
     *
     * @param fxml The fxml file to be loaded
     * @throws NullPointerException Throws if pane is not given at construction
     */
    public void changePanel(String fxml) throws NullPointerException {
        try {
            changePanel(loadFXML(fxml));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Helps with loading a given fxml file as a FXML component
     *
     * @param fxml The fxml file to be loaded
     * @return The loaded fxml component
     * @throws IOException Throws if it fails to read the given resource
     */
    public Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(resource.getResource(VIEW_ROOT + "/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }
}
