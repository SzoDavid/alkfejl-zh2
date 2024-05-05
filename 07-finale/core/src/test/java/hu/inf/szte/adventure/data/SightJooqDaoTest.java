package hu.inf.szte.adventure.data;

import hu.inf.szte.adventure.helper.KeepAliveConnection;
import hu.inf.szte.adventure.model.Sight;
import hu.inf.szte.adventure.util.cfg.ConfigSupport;
import hu.inf.szte.adventure.util.db.SqlDbSupport;
import org.jooq.SQLDialect;
import org.junit.jupiter.api.*;
import org.sqlite.JDBC;
import org.sqlite.SQLiteDataSource;

import javax.sql.DataSource;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SightJooqDaoTest {

    private static final SQLDialect sqlDialect = SQLDialect.SQLITE;
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
        var dao = new SightJooqDao(dataSource, sqlDialect);
        var model = new Sight(null, "My stars!", BigDecimal.valueOf(99.9), 8, 16, "The sky", 8);
        dao.save(model);
        assertThat(model.getId()).isNotNull();
        assertThat(model.getId()).isNotZero();
        assertThat(model.getName()).isEqualTo("My stars!");
        assertThat(model.getPrice().doubleValue()).isEqualTo(99.9);
        assertThat(model.getOpeningHour()).isEqualTo(8);
        assertThat(model.getClosingHour()).isEqualTo(16);
        assertThat(model.getDescription()).isEqualTo("The sky");
        assertThat(model.getPopularity()).isEqualTo(8);
        assertThat(dao.count()).isEqualTo(6);
    }

    @Test
    void findById() {
        var dao = new SightJooqDao(dataSource, sqlDialect);
        var optionalModel = dao.findById(1L);
        assertThat(optionalModel).isNotEmpty();
        var model = optionalModel.get();
        assertThat(model.getId()).isEqualTo(1L);
        assertThat(model.getName()).isEqualTo("Some dump in the neighbourhood");
        assertThat(model.getPrice().doubleValue()).isEqualTo(100.56);
        assertThat(model.getOpeningHour()).isEqualTo(0);
        assertThat(model.getClosingHour()).isEqualTo(24);
        assertThat(model.getDescription()).isEqualTo("A dump");
        assertThat(model.getPopularity()).isEqualTo(10);
        assertThat(dao.findById(0L)).isEmpty();
    }

    @Test
    void findAll() {
        var dao = new SightJooqDao(dataSource, sqlDialect);
        assertThat(dao.count()).isEqualTo(5);
        assertThat(dao.findAll()).allSatisfy(sight -> assertThat(sight).isInstanceOf(Sight.class));
    }

    @Test
    void findAllByIds() {
        var dao = new SightJooqDao(dataSource, sqlDialect);
        assertThat(dao.findAllByIds(List.of(2L, 4L, 5L))).hasSize(3);
        assertThat(dao.findAllByIds(List.of(20L, 4L, 5L))).hasSize(2);
        assertThat(dao.findAllByIds(List.of(0L, 40L, 0L))).hasSize(0);
    }

    @Test
    void findAllByCrit() {
        var dao = new SightJooqDao(dataSource, sqlDialect);
        var filterModel = new Sight();
        filterModel.setOpeningHour(8);
        assertThat(dao.findAllByCrit(filterModel)).hasSize(2);
        filterModel.setId(2L);
        assertThat(dao.findAllByCrit(filterModel)).hasSize(1);
        assertThat(dao.findAllByCrit(new Sight())).hasSize(5);
    }

    @Test
    void deleteById() {
        var dao = new SightJooqDao(dataSource, sqlDialect);
        assertThat(dao.deleteById(0L)).isEqualTo(0);
        assertThat(dao.deleteById(1L)).isEqualTo(1);
        assertThat(dao.deleteById(2L)).isEqualTo(1);
        assertThat(dao.count()).isEqualTo(3);
    }

    @Test
    void deleteAllByIds() {
        var dao = new SightJooqDao(dataSource, sqlDialect);
        assertThat(dao.deleteAllByIds(List.of())).isEqualTo(0);
        assertThat(dao.deleteAllByIds(List.of(1L, 3L, 4L))).isEqualTo(3);
        assertThat(dao.count()).isEqualTo(2);
    }

    @Test
    void updateById() {
        var dao = new SightJooqDao(dataSource, sqlDialect);
        var updater = new Sight();
        updater.setName("Your new name");
        assertThat(dao.updateById(2L, updater)).isEqualTo(1);
        assertThat(dao.findById(2L)).isNotEmpty().get().hasFieldOrPropertyWithValue("name", "Your new name");
        updater.setDescription("Epic description!");
        assertThat(dao.updateById(2L, updater)).isEqualTo(1);
        assertThat(dao.findById(2L)).isNotEmpty().get().hasFieldOrPropertyWithValue("description", "Epic description!");
    }

    @Test
    void updateAllByIds() {
        var dao = new SightJooqDao(dataSource, sqlDialect);
        var updater = new Sight();
        updater.setName("Your new name");
        assertThat(dao.updateAllByIds(List.of(), updater)).isEqualTo(5);
        assertThat(dao.findAll()).allSatisfy(sight -> assertThat(sight).hasFieldOrPropertyWithValue("name", "Your new name"));
    }

    @Test
    void updateAllByIds_2() {
        var dao = new SightJooqDao(dataSource, sqlDialect);
        var updater = new Sight();
        updater.setName("Your new name");
        assertThat(dao.updateAllByIds(List.of(1L, 3L), updater)).isEqualTo(2);
        assertThat(dao.findAllByIds(List.of(1L, 3L))).allSatisfy(sight -> assertThat(sight).hasFieldOrPropertyWithValue("name", "Your new name"));
        assertThat(dao.findById(2L)).isNotEmpty().get().satisfies(sight -> assertThat(sight.getName()).isNotEqualTo("Your new name"));
    }

    @Test
    void prune() {
        var dao = new SightJooqDao(dataSource, sqlDialect);
        assertThat(dao.prune()).isEqualTo(5);
        assertThat(dao.count()).isEqualTo(0);
    }

    @Test
    void count() {
        var dao = new SightJooqDao(dataSource, sqlDialect);
        assertThat(dao.count()).isEqualTo(5);
    }
}
