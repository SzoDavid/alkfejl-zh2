package hu.inf.szte.adventure.util.db;


import javax.sql.DataSource;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Objects;
import java.util.stream.Collectors;

public final class SqlDbSupport {

    private final DataSource dataSource;

    public SqlDbSupport(DataSource ds) {
        dataSource = ds;
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
