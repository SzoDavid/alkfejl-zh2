package hu.inf.szte.controller;

import hu.inf.szte.data.Dao;
import hu.inf.szte.data.DummyJooqDao;
import hu.inf.szte.model.Dummy;
import hu.inf.szte.service.SingletonDummyBinder;
import hu.inf.szte.service.SingletonSqliteDataSource;
import hu.inf.szte.util.fx.TableSupport;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import org.jooq.SQLDialect;

public class DummyReadController {

    private final Dao<Dummy> dao;

    @FXML
    private TableView<Dummy> dummyTableView;

    public DummyReadController() {
        dao = new DummyJooqDao(SingletonSqliteDataSource.getInstance().getDataSource(), SQLDialect.SQLITE);
    }

    @FXML
    private void initialize() {
        TableSupport.from(Dummy.class, dummyTableView).createSchema();
        SingletonDummyBinder.getInstance().dataProperty().set(FXCollections.observableList(dao.findAll()));
        dummyTableView.setItems(SingletonDummyBinder.getInstance().dataProperty());
    }
}
