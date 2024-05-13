package hu.inf.szte.controller;

import hu.inf.szte.data.Dao;
import hu.inf.szte.data.DummyJooqDao;
import hu.inf.szte.model.Dummy;
import hu.inf.szte.service.SingletonDummyBinder;
import hu.inf.szte.service.SingletonSqliteDataSource;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.jooq.SQLDialect;

public class DummyAddController {

    private final Dao<Dummy> dao;

    @FXML
    private TextField textValueTextField;
    @FXML
    private Spinner<Integer> integerValueSpinner;
    @FXML
    private Spinner<Double> doubleValueSpinner;
    @FXML
    private CheckBox booleanValueCheckBox;

    public DummyAddController() {
        dao = new DummyJooqDao(SingletonSqliteDataSource.getInstance().getDataSource(), SQLDialect.SQLITE);
    }

    @FXML
    private void initialize() {
        integerValueSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(
                Integer.MIN_VALUE, Integer.MAX_VALUE, 0));
        doubleValueSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(
                -Double.MAX_VALUE, Double.MAX_VALUE, 0));
    }

    @FXML
    public void onCancel() {
        destroy();
    }

    @FXML
    public void onSave() {
        dao.save(new Dummy(null,
                textValueTextField.getText(),
                integerValueSpinner.getValue(),
                doubleValueSpinner.getValue(),
                booleanValueCheckBox.isSelected()));
        SingletonDummyBinder.getInstance()
                .dataProperty()
                .set(FXCollections.observableList(dao.findAll()));
        destroy();
    }

    private void destroy() {
        ((Stage) textValueTextField.getScene().getWindow()).close();
    }
}
