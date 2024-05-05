package hu.inf.szte.adventure.data;

import hu.inf.szte.adventure.helper.KeepAliveConnection;
import hu.inf.szte.adventure.util.cfg.ConfigSupport;
import hu.inf.szte.adventure.util.db.SqlDbSupport;
import org.junit.jupiter.api.*;
import org.sqlite.SQLiteDataSource;

import javax.sql.DataSource;

class TripJooqDaoTest {

    private static DataSource dataSource;
    private static KeepAliveConnection keepAlive;

    @BeforeAll
    static void setup() {
        keepAlive = new KeepAliveConnection();
        var ds = new SQLiteDataSource();
        ds.setUrl(ConfigSupport.getDbUrl());
        dataSource = ds;
    }

    @AfterAll
    static void cleanup() throws Exception {
        try {
            if (dataSource instanceof AutoCloseable) {
                ((AutoCloseable) dataSource).close();
            }
        } finally {
            keepAlive.close();
        }
    }

    @BeforeEach
    void setUp() {
        var dbSupport = new SqlDbSupport(dataSource);
        dbSupport.createTablesIfNotExist();
        dbSupport.insertIntoTables();
    }

    @AfterEach
    void tearDown() {
        var dbSupport = new SqlDbSupport(dataSource);
        dbSupport.dropTablesIfExist();
    }

    @Test
    void save() {
    }

    @Test
    void findById() {
    }

    @Test
    void findAll() {
    }

    @Test
    void findAllByIds() {
    }

    @Test
    void findAllByCrit() {
    }

    @Test
    void deleteById() {
    }

    @Test
    void deleteAllByIds() {
    }

    @Test
    void updateById() {
    }

    @Test
    void updateAllByIds() {
    }

    @Test
    void prune() {
    }

    @Test
    void count() {
    }
}
