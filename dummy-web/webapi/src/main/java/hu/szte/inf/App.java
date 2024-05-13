package hu.szte.inf;

import hu.szte.inf.core.util.cfg.ConfigSupport;
import hu.szte.inf.core.util.db.SqlDbSupport;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class App {

    private static void tryInitMemDb() {
        // a dum test for checking memory mode db
        if (ConfigSupport.getDbUrl().contains("mode=memory")) {
            SqlDbSupport.initDb();
        }
    }

    public static void main(String[] args) {
        tryInitMemDb();
        new SpringApplication(App.class).run(args);
    }
}
