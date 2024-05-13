package hu.szte.inf.core.service;

import javax.sql.DataSource;

/**
 * <a href="https://www.youtube.com/watch?v=tysmwGx7TNU&t=157s">https://www.youtube.com/watch?v=tysmwGx7TNU&t=157s</a>
 */
public interface DSService extends AutoCloseable {

    DataSource getDataSource();

    @Override
    default void close() throws Exception {
        if (this.getDataSource() != null && this.getDataSource() instanceof AutoCloseable) {
            ((AutoCloseable) this.getDataSource()).close();
        }
    }
}
