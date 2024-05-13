package hu.inf.szte.util.db;


import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Helps to execute multi-line sql scripts from java code.
 * This class will probably be most useful when you have an
 * in-memory db.
 */
public class SqlDbSupport {

    private final DataSource dataSource;

    public SqlDbSupport(DataSource ds) {
        dataSource = ds;
    }

    /**
     * Reads sql commands from a given stream,
     * and executes them accordingly.
     *
     * @param s The input stream to be read
     */
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

    public void createTablesIfNotExist() {
        try (var stream = getClass().getClassLoader().getResourceAsStream("create.sql")) {
            executeSqlFile(stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void dropTablesIfExist() {
        try (var stream = getClass().getClassLoader().getResourceAsStream("drop.sql")) {
            executeSqlFile(stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertIntoTables() {
        try (var stream = getClass().getClassLoader().getResourceAsStream("insert.sql")) {
            executeSqlFile(stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
