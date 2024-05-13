package hu.szte.inf.core.data;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import hu.szte.inf.core.util.cfg.ConfigSupport;

import javax.sql.DataSource;

/**
 * DataSource factory providing HikariCP based data source.
 * It's a connection pool. Nice to have one on a webserver.
 * NOTE: you should not be needing this.
 *       Use {@link hu.szte.inf.core.util.common.Instancer} instead.
 */
public class HikariDataSourceFactory {

    private final HikariConfig config;

    private static HikariConfig getDefaultConfig() {
        var cfg = new HikariConfig();
        cfg.setMinimumIdle(1);
        cfg.setMaximumPoolSize(16);
        cfg.setJdbcUrl(ConfigSupport.getDbUrl());
        return cfg;
    }

    public HikariDataSourceFactory() {
        this(getDefaultConfig());
    }

    public HikariDataSourceFactory(HikariConfig cfg) {
        config = cfg;
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
