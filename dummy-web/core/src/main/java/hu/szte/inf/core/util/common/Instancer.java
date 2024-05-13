package hu.szte.inf.core.util.common;

import hu.szte.inf.core.data.DummyJooqDao;
import hu.szte.inf.core.service.SingletonHikariDataSource;
import hu.szte.inf.core.service.SingletonSQLiteJDBCDataSource;
import org.jooq.SQLDialect;

/**
 * Use this class to create data access objects.
 * Equivalent of creating the *false* controller.
 * E.g.
 * {@code Dao<Long, Subject> dao = Instancer.defaultDaoWithHikariDs();}
 */
public class Instancer {

    /**
     * Get your dao instance for free.
     * The dao will use {@link com.zaxxer.hikari.HikariDataSource} as the datasource. It's a connection pool. It's cool.
     *
     * @return jOOQ implementation of this dao
     */
    public static DummyJooqDao defaultDaoWithHikariDs() {
        return new DummyJooqDao(SingletonHikariDataSource.getInstance().getDataSource(), SQLDialect.SQLITE);
    }

    /**
     * Get your dao instance for free.
     * The dao will use a simple {@link org.sqlite.SQLiteDataSource}.
     *
     * @return jOOQ implementation of this dao
     */
    public static DummyJooqDao defaultDaoWithSimpleDs() {
        return new DummyJooqDao(SingletonSQLiteJDBCDataSource.getInstance().getDataSource(), SQLDialect.SQLITE);
    }
}
