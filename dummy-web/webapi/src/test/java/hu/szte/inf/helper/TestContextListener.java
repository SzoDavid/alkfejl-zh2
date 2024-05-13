package hu.szte.inf.helper;

import hu.szte.inf.core.util.db.SqlDbSupport;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class TestContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        SqlDbSupport.dropDb();
    }
}
