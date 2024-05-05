package hu.inf.szte.adventure.listener;

import hu.inf.szte.adventure.data.HikariDataSourceFactory;
import hu.inf.szte.adventure.util.db.SqlDbSupport;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import javax.sql.DataSource;

@WebListener
public class ContextListener implements ServletContextListener {

    private DataSource ds;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ds = new HikariDataSourceFactory().getDataSource();
        sce.getServletContext().setAttribute("ds", ds);
        var db = new SqlDbSupport(ds);
        db.createTablesIfNotExist();
        db.insertIntoTables();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (ds != null && ds instanceof AutoCloseable) {
            try {
                ((AutoCloseable) ds).close();
            } catch (Exception e) {
                // wtf?
                throw new RuntimeException(e);
            }
        }
    }
}
