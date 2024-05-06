package hu.inf.szte.listener;

import hu.inf.szte.core.data.HikariDataSourceFactory;
import hu.inf.szte.core.util.db.SqlDbSupport;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import javax.sql.DataSource;

@WebListener
public class ContextListener implements ServletContextListener {
    private DataSource dataSource;
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        dataSource = new HikariDataSourceFactory().getDataSource();
        sce.getServletContext().setAttribute("ds", dataSource);
        var db = new SqlDbSupport(dataSource);
        db.createTablesIfNotExist();
        db.insertIntoTables();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (dataSource != null && dataSource instanceof AutoCloseable) {
            try {
                ((AutoCloseable) dataSource).close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
