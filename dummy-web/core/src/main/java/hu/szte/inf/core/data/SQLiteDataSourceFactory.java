package hu.szte.inf.core.data;

import hu.szte.inf.core.util.cfg.ConfigSupport;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;

import javax.sql.DataSource;

/**
 * DataSource factory providing SQLiteJDBC based data source.
 * No connection pools. Just a simple datasource. A desert.
 * NOTE: you should not be needing this.
 *       Use {@link hu.szte.inf.core.util.common.Instancer} instead.
 */
public class SQLiteDataSourceFactory {

    private final SQLiteConfig config;
    private final String url;

    public SQLiteDataSourceFactory() {
        this(ConfigSupport.getDbUrl(), new SQLiteConfig());
    }

    public SQLiteDataSourceFactory(SQLiteConfig cfg) {
        this(ConfigSupport.getDbUrl(), cfg);
    }

    public SQLiteDataSourceFactory(String conn) {
        this(conn, null);
    }

    public SQLiteDataSourceFactory(String conn, SQLiteConfig cfg) {
        url = conn;
        config = cfg;
    }

    /**
     * Creates a new {@link DataSource} instance
     * based on the provided configuration.
     *
     * @return {@link DataSource} object
     */
    public SQLiteDataSource getDataSource() {
        var ds = new SQLiteDataSource(config);
        if (url != null) {
            ds.setUrl(url);
        }
        return ds;
    }
}
