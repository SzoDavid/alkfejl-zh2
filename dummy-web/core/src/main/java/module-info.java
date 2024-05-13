module hu.szte.inf.core {
    requires static lombok;
    requires static org.xerial.sqlitejdbc;
    requires com.zaxxer.hikari;
    requires org.jooq;
    requires transitive java.sql;

    exports hu.szte.inf.core.model;
    exports hu.szte.inf.core.data;
    exports hu.szte.inf.core.util.db;
    exports hu.szte.inf.core.util.cfg;
    exports hu.szte.inf.core.util.common;
    exports hu.szte.inf.core.service;

    opens hu.szte.inf.core.model;
}
