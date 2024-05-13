package hu.szte.inf.core.util.db;


import hu.szte.inf.core.service.SingletonSQLiteJDBCDataSource;

import javax.sql.DataSource;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * You should only need this if you use an in-memory database.
 * In that case calling dbInit() should be enough.
 */
public final class SqlDbSupport {

    private final DataSource dataSource;

    public SqlDbSupport(DataSource ds) {
        dataSource = ds;
    }

    /**
     * Use this method to initialize database with default tables and values.
     * Either in-memory, and on-disk db should work just fine.
     */
    public static void initDb() {
        String sql = """
                DROP TABLE IF EXISTS dummy;
                CREATE TABLE IF NOT EXISTS dummy (
                    `id` INTEGER NOT NULL,
                    `some_string` TEXT NOT NULL,
                    `another_string` TEXT UNIQUE NOT NULL,
                    `some_bool` BOOLEAN NOT NULL,
                    `some_int` INT NOT NULL,
                    `other_int` INT,
                    PRIMARY KEY (`id`)
                );
                INSERT INTO dummy (some_string, another_string, some_bool, some_int, other_int) VALUES ("sjkad ka sjdwaUI", "dsad wa", 1, 4, 2);
                INSERT INTO dummy (some_string, another_string, some_bool, some_int, other_int) VALUES ("jkask dakwu .", "dsiakd jawj", 1, 5, 4);
                INSERT INTO dummy (some_string, another_string, some_bool, some_int, other_int) VALUES ("dw aid DUWAI Dsa.", "pwadojsakm sa", 1, 7, 5);
                INSERT INTO dummy (some_string, another_string, some_bool, some_int, other_int) VALUES ("wdai dwaidawd ", "soidak sadzwajd", 1, 3, 3);
                INSERT INTO dummy (some_string, another_string, some_bool, some_int, other_int) VALUES ("iwad aidwa ndsajw", "DWAIdbsadbiwz", 0, 10, NULL);
                INSERT INTO dummy (some_string, another_string, some_bool, some_int, other_int) VALUES ("iwaod waduadwa dwua", "KLYNDEC", 1, 12, 5);
                INSERT INTO dummy (some_string, another_string, some_bool, some_int, other_int) VALUES ("dwuia dwaudadwa dwadw", "dAK waDUW", 0, 6, NULL);
                INSERT INTO dummy (some_string, another_string, some_bool, some_int, other_int) VALUES ("WAIOdhnsad woadua dsa", "DWUAIDksaj daw", 1, 5, 2);
                """;
        new SqlDbSupport(SingletonSQLiteJDBCDataSource.getInstance().getDataSource()).executeSqlStrings(sql);
    }

    /**
     * Drop default tables from db.
     */
    public static void dropDb() {
        String sql = """
                DROP TABLE IF EXISTS dummy;
                """;
        new SqlDbSupport(SingletonSQLiteJDBCDataSource.getInstance().getDataSource()).executeSqlStrings(sql);
    }

    public static SqlDbSupport withDataSource(DataSource ds) {
        return new SqlDbSupport(ds);
    }

    public void executeSqlFile(InputStream s) {
        try (var reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(s)));
             var conn = dataSource.getConnection();
             var stmt = conn.createStatement()) {
            var sql = reader.lines().collect(Collectors.joining());
            var sqlCommands = sql.split("(?<=;)");
            for (var c : sqlCommands) {
                stmt.addBatch(c);
            }
            stmt.executeBatch();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void executeSqlStrings(String s) {
        executeSqlFile(new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8)));
    }
}
