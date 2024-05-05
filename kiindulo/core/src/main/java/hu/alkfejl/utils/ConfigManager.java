package hu.alkfejl.utils;

import java.io.IOException;
import java.util.Properties;

/**
 * Segédosztály, config-ok betöltéséhez.
 * */
public class ConfigManager {
    private Properties props = new Properties();

    /**
     * Konstruktor.
     * Létrehoz egy objektumot, ami betölti a config-ot és azon config alapján lehet értékeket leolvasni
     * @param c Class, ami alapján a config-ot megtalálja az objektum. Az adott class contextusához tartozó resource-okat használja.
     *          pontosabban az application.properties fájlt.
     * */
    public ConfigManager(Class<?> c) {
        try {
            props.load(c.getResourceAsStream("/application.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Függvény amivel lekérhetők config-ok.
     * @param key a config neve ami alapján lekérhetjük az értéket. configNev=ertek
     *            Nincsen "" a string értékek körül!
     * */
    public String getValue(String key){
        return props.getProperty(key);
    }
}
