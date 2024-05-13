package hu.szte.inf.core.service;

import com.zaxxer.hikari.HikariDataSource;
import hu.szte.inf.core.data.HikariDataSourceFactory;

import javax.sql.DataSource;

/**
 * NOTE: you should not be needing this.
 *       Use {@link hu.szte.inf.core.util.common.Instancer} instead.
 */
public class SingletonHikariDataSource implements DSService {

    private final HikariDataSource dataSource;

    private SingletonHikariDataSource() {
        dataSource = new HikariDataSourceFactory().getDataSource();
    }

    public static SingletonHikariDataSource getInstance() {
        return Instance.INSTANCE;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    private static final class Instance {
        private static final SingletonHikariDataSource INSTANCE = new SingletonHikariDataSource();
    }
}
