package hu.inf.szte.adventure.controller;

import hu.inf.szte.adventure.client.SightClientDao;
import hu.inf.szte.adventure.client.SingletonHttpClient;
import hu.inf.szte.adventure.data.Dao;
import hu.inf.szte.adventure.model.Sight;
import hu.inf.szte.adventure.service.SingletonSightDataBinding;
import javafx.beans.property.ListProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.util.stream.StreamSupport;

public class SightAddController {

    private final Dao<Long, Sight> dao;
    private final ListProperty<Sight> bindingList;

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

    public SightAddController() {
        dao = new SightClientDao(SingletonHttpClient.getInstance().getClient());
        bindingList = SingletonSightDataBinding.getInstance().bindingListProperty();
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
