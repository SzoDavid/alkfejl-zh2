package hu.inf.szte.adventure.controller;

import hu.inf.szte.adventure.client.SightClientDao;
import hu.inf.szte.adventure.client.SingletonHttpClient;
import hu.inf.szte.adventure.data.Dao;
import hu.inf.szte.adventure.model.Sight;
import hu.inf.szte.adventure.service.SingletonSightDataBinding;
import javafx.beans.property.ListProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.util.stream.StreamSupport;

public class SightUpdateController {

    private final Dao<Long, Sight> dao;
    private final ListProperty<Sight> bindingList;
    private final Sight updateModel;

    @FXML
    private TextField idTextField;
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

    public SightUpdateController(Sight model) {
        updateModel = model;
        dao = new SightClientDao(SingletonHttpClient.getInstance().getClient());
        bindingList = SingletonSightDataBinding.getInstance().bindingListProperty();
    }

    @FXML
    private void initialize() {
        idTextField.textProperty().set(updateModel.getId().toString());
        nameTextField.setText(updateModel.getName());
        priceSpinner.getValueFactory().setValue(updateModel.getPrice().doubleValue());
        openingHourSpinner.getValueFactory().setValue(updateModel.getOpeningHour());
        closingHourSpinner.getValueFactory().setValue(updateModel.getClosingHour());
        descriptionTextArea.setText(updateModel.getDescription());
        popularitySpinner.getValueFactory().setValue(updateModel.getPopularity());
    }

    private Sight readInputFields() {
        return new Sight(null,
                nameTextField.getText(),
                BigDecimal.valueOf(priceSpinner.getValue()),
                openingHourSpinner.getValue(),
                closingHourSpinner.getValue(),
                descriptionTextArea.getText(),
                popularitySpinner.getValue());
    }

    @FXML
    private void onSave() {
        // TODO: catch and handle possible exceptions
        dao.updateById(updateModel.getId(), readInputFields());
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
