package hu.inf.szte.adventure.data;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import hu.inf.szte.adventure.util.cfg.ConfigSupport;

import javax.sql.DataSource;

/**
 * DataSource factory providing HikariCP based data source.
 * It's a connection pool. Nice to have one on a webserver.
 */
public class HikariDataSourceFactory {

    private final HikariConfig config;

    public HikariDataSourceFactory() {
        config = new HikariConfig();
        config.setJdbcUrl(ConfigSupport.getDbUrl());
        config.setMaximumPoolSize(16);
    }

    public HikariDataSourceFactory(HikariConfig c) {
        config = c;
    }

    /**
     * Creates a new {@link DataSource} instance
     * based on the provided configuration.
     *
     * @return {@link DataSource} object
     */
    public HikariDataSource getDataSource() {
        return new HikariDataSource(config);
    }
}
