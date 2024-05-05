package hu.inf.szte.adventure.controller;

import hu.inf.szte.adventure.client.PrefClientDao;
import hu.inf.szte.adventure.client.SightClientDao;
import hu.inf.szte.adventure.client.SingletonHttpClient;
import hu.inf.szte.adventure.data.Dao;
import hu.inf.szte.adventure.model.Preferences;
import hu.inf.szte.adventure.model.Sight;
import hu.inf.szte.adventure.service.SingletonSightDataBinding;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.NumberStringConverter;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.stream.StreamSupport;

public class PreferenceController {

    private final Dao<Object, Preferences> dao;
    private final Dao<Long, Sight> sightDao;
    private final PrefProp pref = new PrefProp();
    private final ListProperty<Sight> bindingList;

    @FXML
    private Slider minPriceSlider;
    @FXML
    private Slider maxPriceSlider;
    @FXML
    private Slider minPopularitySlider;
    @FXML
    private Spinner<Double> minPriceSpinner;
    @FXML
    private Spinner<Double> maxPriceSpinner;
    @FXML
    private Spinner<Integer> minPopularitySpinner;

    public PreferenceController() {
        dao = new PrefClientDao(SingletonHttpClient.getInstance().getClient());
        sightDao = new SightClientDao(SingletonHttpClient.getInstance().getClient());
        bindingList = SingletonSightDataBinding.getInstance().bindingListProperty();
    }

    @FXML
    private void initialize() {
        minPriceSpinner.setValueFactory(
                new SpinnerValueFactory.DoubleSpinnerValueFactory(minPriceSlider.getMin(), minPriceSlider.getMax()));
        maxPriceSpinner.setValueFactory(
                new SpinnerValueFactory.DoubleSpinnerValueFactory(maxPriceSlider.getMin(), maxPriceSlider.getMax()));
        minPopularitySpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory((int) minPopularitySlider.getMin(), (int) minPopularitySlider.getMax()));

        Bindings.bindBidirectional(pref.minPrice, minPriceSpinner.getValueFactory().valueProperty(), new DoubleStringConverter());
        Bindings.bindBidirectional(pref.maxPrice, maxPriceSpinner.getValueFactory().valueProperty(), new DoubleStringConverter());
        Bindings.bindBidirectional(pref.minPopularity, minPopularitySpinner.getValueFactory().valueProperty(), new IntegerStringConverter());
        Bindings.bindBidirectional(pref.minPrice, minPriceSlider.valueProperty(), new NumberStringConverter());
        Bindings.bindBidirectional(pref.maxPrice, maxPriceSlider.valueProperty(), new NumberStringConverter());
        Bindings.bindBidirectional(pref.minPopularity, minPopularitySlider.valueProperty(), new StringConverter<>() {
            @Override
            public String toString(Number number) {
                return Integer.toString(number.intValue());
            }

            @Override
            public Number fromString(String s) {
                try {
                    return NumberFormat.getInstance().parse(s).intValue();
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private Preferences getPreferenceState() {
        return new Preferences(BigDecimal.valueOf(minPriceSlider.getValue()),
                BigDecimal.valueOf(maxPriceSlider.getValue()),
                (int) minPopularitySlider.getValue());
    }

    @FXML
    private void onApply() {
        // TODO: handle exceptions
        dao.save(getPreferenceState());
        bindingList.set(FXCollections.observableList(
                StreamSupport.stream(sightDao.findAll().spliterator(), false).toList()));
        destroy();
    }

    @FXML
    private void onReset() {
        // TODO: handle exceptions
        dao.prune();
        bindingList.set(FXCollections.observableList(
                StreamSupport.stream(sightDao.findAll().spliterator(), false).toList()));
        destroy();
    }

    private void destroy() {
        ((Stage) minPriceSlider.getScene().getWindow()).close();
    }

    private static final class PrefProp {
        SimpleStringProperty minPrice = new SimpleStringProperty();
        SimpleStringProperty maxPrice = new SimpleStringProperty();
        SimpleStringProperty minPopularity = new SimpleStringProperty();
    }
}
