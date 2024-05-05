package hu.inf.szte.adventure.controller;

import hu.inf.szte.adventure.client.SingletonHttpClient;
import hu.inf.szte.adventure.client.TripClientDao;
import hu.inf.szte.adventure.data.Dao;
import hu.inf.szte.adventure.model.Trip;
import hu.inf.szte.adventure.service.SingletonTripDataBinding;
import javafx.beans.property.ListProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.stream.StreamSupport;

public class TripAddController {

    private final Dao<Long, Trip> dao;
    private final ListProperty<Trip> bindingList;

    @FXML
    private TextField nameTextField;
    @FXML
    private CheckBox halfBoardCheckBox;
    @FXML
    private Spinner<Integer> numGuestsSpinner;
    @FXML
    private Spinner<Integer> numNightsSpinner;
    @FXML
    private TextArea descriptionTextArea;

    public TripAddController() {
        dao = new TripClientDao(SingletonHttpClient.getInstance().getClient());
        bindingList = SingletonTripDataBinding.getInstance().bindingListProperty();
    }

    private Trip readInputFields() {
        return new Trip(null,
                nameTextField.getText(),
                halfBoardCheckBox.isSelected(),
                numGuestsSpinner.getValue(),
                numNightsSpinner.getValue(),
                descriptionTextArea.getText());
    }

    @FXML
    private void onSave() {
        // TODO: catch and handle possible exceptions
        dao.save(readInputFields());
        bindingList.set(FXCollections.observableList(
                StreamSupport.stream(dao.findAll().spliterator(), false).toList()));
        destroy();
    }

    @FXML
    private void onCancel() {
        destroy();
    }

    private void destroy() {
        ((Stage) nameTextField.getScene().getWindow()).close();
    }
}
