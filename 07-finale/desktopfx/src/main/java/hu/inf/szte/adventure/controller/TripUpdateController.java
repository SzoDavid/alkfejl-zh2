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

public class TripUpdateController {

    private final Dao<Long, Trip> dao;
    private final ListProperty<Trip> bindingList;
    private final Trip updateModel;

    @FXML
    private TextField idTextField;
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

    public TripUpdateController(Trip model) {
        updateModel = model;
        dao = new TripClientDao(SingletonHttpClient.getInstance().getClient());
        bindingList = SingletonTripDataBinding.getInstance().bindingListProperty();
    }

    @FXML
    private void initialize() {
        idTextField.textProperty().set(updateModel.getId().toString());
        nameTextField.setText(updateModel.getName());
        halfBoardCheckBox.setSelected(updateModel.getHalfBoard());
        numGuestsSpinner.getValueFactory().setValue(updateModel.getNumGuests());
        numNightsSpinner.getValueFactory().setValue(updateModel.getNumNights());
        descriptionTextArea.setText(updateModel.getDescription());
    }

    private Trip readInputFields() {
        return new Trip(updateModel.getId(),
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
