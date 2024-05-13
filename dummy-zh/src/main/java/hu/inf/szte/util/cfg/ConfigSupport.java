package hu.inf.szte.util.cfg;

import java.io.IOException;
import java.util.Properties;

/**
 * Loads and reads an application.properties file.
 * This file will be searched for in the resources root:
 * `src/main/resources/application.properties`
 */
public final class ConfigSupport {

    // A placeholder for the read properties
    private static final Properties props = new Properties();

    static {
        try (var stream = ConfigSupport.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            props.load(stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Extracts property `db.url` from the properties file.
     *
     * @return Url for db connection defined in the application.properties file.
     */
    public static String getDbUrl() {
        return props.getProperty("db.url");
    }
}
