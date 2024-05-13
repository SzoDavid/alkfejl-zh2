module hu.inf.szte {
    requires lombok;
    requires org.jooq;
    requires java.sql;
    requires javafx.controls;
    requires javafx.fxml;
    requires com.zaxxer.hikari;
    requires org.xerial.sqlitejdbc;

    opens hu.inf.szte to javafx.fxml;
    opens hu.inf.szte.controller to javafx.fxml;
    opens hu.inf.szte.model to javafx.base;

    exports hu.inf.szte;
}
