package hu.inf.szte.service;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;

@Getter
public class SingletonHikariDataSource implements AutoCloseable {

    private final HikariDataSource dataSource;

    private SingletonHikariDataSource() {
        var conf = new HikariConfig();
        conf.setMaximumPoolSize(16);
        dataSource = new HikariDataSource(conf);
    }

    public static SingletonHikariDataSource getInstance() {
        return SingletonInstance.INSTANCE;
    }

    @Override
    public void close() {
        dataSource.close();
    }

    private static final class SingletonInstance {
        private static final SingletonHikariDataSource INSTANCE = new SingletonHikariDataSource();
    }
}
