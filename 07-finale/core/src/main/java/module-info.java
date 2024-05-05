module hu.inf.szte.adventure.core {

    requires static lombok;
    requires org.jooq;
    requires transitive java.sql;
    requires static org.xerial.sqlitejdbc;
    requires transitive com.zaxxer.hikari;

    exports hu.inf.szte.adventure.auth;
    exports hu.inf.szte.adventure.model;
    exports hu.inf.szte.adventure.data;
    exports hu.inf.szte.adventure.exception;
    exports hu.inf.szte.adventure.util.db;
    exports hu.inf.szte.adventure.util.cfg;

    opens hu.inf.szte.adventure.model;
    opens hu.inf.szte.adventure.exception;
}
