package hu.inf.szte.adventure.controller;

import hu.inf.szte.adventure.App;
import hu.inf.szte.adventure.client.SightClientDao;
import hu.inf.szte.adventure.client.SingletonHttpClient;
import hu.inf.szte.adventure.data.Dao;
import hu.inf.szte.adventure.model.Sight;
import hu.inf.szte.adventure.service.SingletonSightDataBinding;
import hu.inf.szte.adventure.util.fx.SceneLoader;
import hu.inf.szte.adventure.util.fx.TableSupport;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.function.Function;
import java.util.stream.StreamSupport;

public class SightListController {

    private final SceneLoader sceneLoader;
    private final Dao<Long, Sight> dao;
    private final ListProperty<Sight> bindingList;

    @FXML
    private Spinner<Integer> idSpinner;
    @FXML
    private TextField nameTextField;
    @FXML
    private Spinner<Double> priceSpinner;
    @FXML
    private Spinner<Integer> openingHourSpinner;
    @FXML
    private Spinner<Integer> closingHourSpinner;
    @FXML
    private TextArea descriptionTextArea;
    @FXML
    private Spinner<Integer> popularitySpinner;
    @FXML
    private TableView<Sight> tableView;

    public SightListController() {
        sceneLoader = new SceneLoader(App.class, null);
        dao = new SightClientDao(SingletonHttpClient.getInstance().getClient());
        bindingList = SingletonSightDataBinding.getInstance().bindingListProperty();
    }

    private Sight readInputFields() {
        return new Sight(idSpinner.getValue() <= 0 ? null : idSpinner.getValue().longValue(),
                nameTextField.getText().isBlank() || nameTextField.getText().isEmpty() ? null : nameTextField.getText(),
                priceSpinner.getValue() <= 0 ? null : BigDecimal.valueOf(priceSpinner.getValue()),
                openingHourSpinner.getValue() <= 0 ? null : openingHourSpinner.getValue(),
                closingHourSpinner.getValue() <= 0 ? null : closingHourSpinner.getValue(),
                descriptionTextArea.getText().isBlank() || descriptionTextArea.getText().isEmpty() ? null : descriptionTextArea.getText(),
                popularitySpinner.getValue() <= 0 ? null : popularitySpinner.getValue());
    }

    @FXML
    private void initialize() {
        TableSupport.from(Sight.class, tableView).createSchema();
        try {
            bindingList.set(FXCollections.observableList(StreamSupport.stream(
                    dao.findAll().spliterator(), false).toList()));
        } catch (RuntimeException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK).showAndWait();
        }
        tableView.setItems(bindingList);
        InvalidationListener changeListener = (listener) -> bindingList.set(
                FXCollections.observableList(StreamSupport.stream(
                        dao.findAllByCrit(readInputFields()).spliterator(), false).toList()));
        idSpinner.getValueFactory().valueProperty().addListener(changeListener);
        nameTextField.textProperty().addListener(changeListener);
        priceSpinner.getValueFactory().valueProperty().addListener(changeListener);
        openingHourSpinner.getValueFactory().valueProperty().addListener(changeListener);
        closingHourSpinner.getValueFactory().valueProperty().addListener(changeListener);
        descriptionTextArea.textProperty().addListener(changeListener);
        popularitySpinner.getValueFactory().valueProperty().addListener(changeListener);

        // Create context menu for update and delete
        var editMenu = new MenuItem("Edit");
        var deleteMenu = new MenuItem("Delete");
        var menu = new ContextMenu(editMenu, deleteMenu);
        editMenu.setOnAction(event -> {
            var model = tableView.selectionModelProperty().get()
                    .selectedItemProperty().get();
            var stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            try {
                stage.setScene(new Scene(sceneLoader.loadFXML("sight/update", clazz -> new SightUpdateController(model))));
                stage.showAndWait();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        deleteMenu.setOnAction(event -> {
            var model = tableView.selectionModelProperty().get()
                    .selectedItemProperty().get();
            var result = new Alert(Alert.AlertType.CONFIRMATION,
                    "Are you sure to delete model %s?".formatted(model),
                    ButtonType.YES,
                    ButtonType.NO)
                    .showAndWait();
            result.ifPresent(btnResult -> {
                // TODO: handle exceptions
                if (btnResult == ButtonType.YES) {
                    dao.deleteById(model.getId());
                }
            });
            bindingList.set(FXCollections.observableList(
                    StreamSupport.stream(dao.findAll().spliterator(), false).toList()));
        });
        // Create double click event for edit
        var editDoubleClick = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton().equals(MouseButton.PRIMARY)) {
                    if (event.getClickCount() == 2) {
                        var model = tableView.selectionModelProperty().get()
                                .selectedItemProperty().get();
                        var stage = new Stage();
                        stage.initModality(Modality.APPLICATION_MODAL);
                        try {
                            stage.setScene(new Scene(sceneLoader.loadFXML("sight/update", clazz -> new SightUpdateController(model))));
                            stage.showAndWait();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        };

        // Say your prayers!
        tableView.setRowFactory(sightTableView -> {
            final TableRow<Sight> row = new TableRow<>();
            // This is where the magic happens.
            // We want to allow edits on double click,
            // while we also allow context menus in
            // non-empty rows.
            // To this end, we simply bind the menu,
            // and mouse click property for an appropriate
            // row-empty-property.
            // Why you ask?
            // Whatever happens to this table at runtime
            // with the use of properties we can make
            // sure that only non-empty rows will be bound
            // with necessary click events, and menus.
            row.contextMenuProperty().bind(
                    Bindings.when(row.emptyProperty())
                            .then((ContextMenu) null)
                            .otherwise(menu));
            row.onMouseClickedProperty().bind(
                    Bindings.when(row.emptyProperty())
                            .then((EventHandler<MouseEvent>) null)
                            .otherwise(editDoubleClick));
            return row;
        });
    }
}
