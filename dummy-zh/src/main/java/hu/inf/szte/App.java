package hu.inf.szte;

import hu.inf.szte.service.SingletonSqliteDataSource;
import hu.inf.szte.util.db.SqlDbSupport;
import hu.inf.szte.util.fx.SceneLoader;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class App extends Application {

    private final SceneLoader sceneLoader = new SceneLoader(App.class, null);

    @Override
    public void start(Stage stage) throws IOException {
        Scene scene = new Scene(sceneLoader.loadFXML("main"));
        stage.setScene(scene);
        stage.setOnCloseRequest(e -> {
            try {
                SingletonSqliteDataSource.getInstance().close();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        stage.show();
    }

    public static void main(String[] args) {
        initDb();
        launch();
    }

    private static void initDb() {
        var dbSupport = new SqlDbSupport(SingletonSqliteDataSource.getInstance().getDataSource());
        dbSupport.createTablesIfNotExist();
        dbSupport.insertIntoTables();
    }
}
