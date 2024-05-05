package hu.inf.szte.adventure.helper;

import hu.inf.szte.adventure.util.cfg.ConfigSupport;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
