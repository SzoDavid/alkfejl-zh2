package hu.inf.szte.adventure.util.cfg;

import java.io.IOException;
import java.util.Properties;

public final class ConfigSupport {

    private static final Properties props = new Properties();

    static {
        try (var stream = ConfigSupport.class.getClassLoader().getResourceAsStream("appconfig.properties")) {
            props.load(stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getProperty(String key) {
        return props.getProperty(key);
    }

    public static String getDbUrl() {
        return getProperty("db.url");
    }
}
