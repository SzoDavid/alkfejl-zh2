package hu.szte.inf.core.service;

import hu.szte.inf.core.data.SQLiteDataSourceFactory;
import hu.szte.inf.core.util.db.KeepAliveConnection;
import org.sqlite.SQLiteDataSource;

import javax.sql.DataSource;

/**
 * NOTE: you should not be needing this.
 *       Use {@link hu.szte.inf.core.util.common.Instancer} instead.
 */
public class SingletonSQLiteJDBCDataSource implements DSService {

    private final SQLiteDataSource dataSource;
    private final KeepAliveConnection keepAlive;

    private SingletonSQLiteJDBCDataSource() {
        keepAlive = new KeepAliveConnection();
        dataSource = new SQLiteDataSourceFactory().getDataSource();
    }

    public static SingletonSQLiteJDBCDataSource getInstance() {
        return Instance.INSTANCE;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    private static final class Instance {
        private static final SingletonSQLiteJDBCDataSource INSTANCE = new SingletonSQLiteJDBCDataSource();
    }

    @Override
    public void close() throws Exception {
        try {
            DSService.super.close();
        }
        catch (Exception e) {
            keepAlive.close();
            throw e;
        }
    }
}
