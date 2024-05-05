package hu.alkfejl.controller;

import hu.alkfejl.dao.ZeneDAO;
import hu.alkfejl.dao.ZeneSQLiteImpl;
import hu.alkfejl.utils.ConfigManager;
import hu.alkfejl.model.Zene;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.System.out;

public class ZeneController {

    private ZeneDAO dao;
    // fontos, hogy interface alapjan egy valtozot hasznalkjunk DAO elereshez
    // igy konnyen tudjuk valtoztatni, illetve
    // dinamikusan tudunk betolteni dao megvalositast
    private static final Map<String, ZeneController> instances = new ConcurrentHashMap<>();

    private ZeneController(String daoToUse, String dbPath) {
        switch(daoToUse) {
            case "sqlite":
                var extendedPath = dbPath.startsWith("jdbc:sqlite:") ? dbPath : "jdbc:sqlite:" + dbPath;
                dao = new ZeneSQLiteImpl(extendedPath); break;
            default:
                try {
                    out.println("Using custom dao: " + daoToUse);
                    // barhogy megadhatjuk a peldanyositast, de fontos, hogy onnantol azt kell kovese az egyedi megvalositas is
                    // igy egy konstuktoron keresztul barmilyen metodus hivhato, erre figyeljunk oda!
                    // ez csak egy pelda, hogy lassuk a dao valtozo interface tipusanak fontossagat!
                    Class<?> c = Class.forName(daoToUse);
                    dao = (ZeneDAO) c.getDeclaredConstructor(String.class).newInstance(dbPath);
                } catch (ClassNotFoundException | IllegalAccessException | InstantiationException |
                         InvocationTargetException | NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
        }
    }

    /**
     *
     * Egy példányt ad vissza a dao és dbPath adatok felhasználásával.
     * @param clazz A kontrollert FELHASZNÁLÓ osztály osztálya.
     *              Ez alapjéán kiolvassa a config fájlt és a dao és dbPath értékeket beállítja, ahhoz ad egy kontrollert.
     *              A daonak az 'sqlite' és 'egyéb' a lehetséges értékeke. SQLite esetében hosszáfűzi a jdbc:sqlite: kezdést ha nem lenne.
     *              Egyéb megadásakor a classpath on egy osztály fulli qualified nevét kell megadni. Ekkor a dbPath érintetlenül kerül átadásra.
     * */
    public static ZeneController getInstance(Class<?> clazz){
        ConfigManager cm = new ConfigManager(clazz);
        var daoToUse = cm.getValue("dao");
        var dbPath = cm.getValue("dbPath");

        ZeneController instance;

        if ( instances.containsKey(daoToUse + dbPath) ) {
            instance = instances.get(daoToUse + dbPath);
        } else {
            instance = new ZeneController(daoToUse, dbPath);
            instances.put(daoToUse + dbPath, instance);
        }

        return instance;
    }

    /**
     * Egy új zenét ad át a kiválasztott dao-nak add-ra. Annak értékét adja vissza.
     * Ha rap a stílus, minden esetben hamisat ad vissza, nem csinál semmit.
     * */
    public boolean add(Zene zene) {
        if ( "rap".equals( zene.getStilus() ) ) {
            return false;
        }
        return dao.add(zene);
    }

    /**
     * Visszaadja a kiválasztott dao által adott listát a keresési filter alapján.
     * A filternek be kell állítani mindent amire szűrni szeretnénk, egy sablon objektumota dunk át.
     * */
    public List<Zene> find(Zene filter) {
        return dao.find(filter);
    }

    /**
     * Clear all dao instances. Not intended for use!
     * */
    public static void clearInstances() {
        instances.clear();
    }
}
