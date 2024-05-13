package hu.szte.inf.core.util.db;

import hu.szte.inf.core.util.cfg.ConfigSupport;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * A connection that should be kept alive
 * to ensure memory db persistence.
 * You should not be need to use this class explicitly.
 */
public class KeepAliveConnection implements AutoCloseable {

    private final Connection connection;

    public KeepAliveConnection() {
        try {
            connection = DriverManager.getConnection(ConfigSupport.getDbUrl());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }
}
