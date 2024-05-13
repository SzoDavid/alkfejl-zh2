package hu.inf.szte.service;

import hu.inf.szte.util.cfg.ConfigSupport;
import lombok.Getter;
import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.SQLException;

@Getter
public class SingletonSqliteDataSource implements AutoCloseable {

    private final SQLiteDataSource dataSource;
    private final Connection keepAlive;

    private SingletonSqliteDataSource() {
        dataSource = new SQLiteDataSource();
        dataSource.setUrl(ConfigSupport.getDbUrl());
        try {
            keepAlive = dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static SingletonSqliteDataSource getInstance() {
        return SingletonInstance.INSTANCE;
    }

    @Override
    public void close() throws SQLException {
        keepAlive.close();
    }

    private static final class SingletonInstance {
        private static final SingletonSqliteDataSource INSTANCE = new SingletonSqliteDataSource();
    }
}
