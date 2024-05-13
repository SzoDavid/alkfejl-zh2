package hu.inf.szte.data;

import hu.inf.szte.model.Dummy;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class DummySqliteJdbcDao implements Dao<Dummy> {

    private static final String TABLE = "dummy";

    private final DataSource dataSource;

    private static Dummy readResult(ResultSet result) throws SQLException {
        return new Dummy(result.getLong("id"),
                result.getString("text_value"),
                result.getInt("integer_value"),
                result.getDouble("double_value"),
                result.getBoolean("bool_value"));
    }

    @Override
    public void save(@NonNull Dummy model) {
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement("INSERT INTO " + TABLE +
                     " (text_value, integer_value, double_value, bool_value) VALUES (?, ?, ?, ?);")) {

            stmt.setString(1, model.getText());
            stmt.setInt(2, model.getNumInt());
            stmt.setDouble(3, model.getNumDouble());
            stmt.setBoolean(4, model.getBool());

            try (var result = stmt.getGeneratedKeys()) {
                if (result.next()) {
                    model.setId(result.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Dummy> findAll() {
        var models = new ArrayList<Dummy>();
        try (var conn = dataSource.getConnection();
             var stmt = conn.createStatement();
             var result = stmt.executeQuery("SELECT * FROM " + TABLE + ";")) {

            while (result.next()) {
                models.add(readResult(result));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return models;
    }

    @Override
    public List<Dummy> findAllByCrit(@NonNull Dummy model) {
        var models = new ArrayList<Dummy>();
        var where = new ArrayList<String>();

        if (model.getId() != null) {
            where.add("id = ?");
        }
        if (model.getText() != null && !model.getText().isEmpty()) {
            where.add("LOWER(text_value) LIKE ?");
        }
        if (model.getNumInt() != null) {
            where.add("integer_value = ?");
        }
        if (model.getNumDouble() != null) {
            where.add("double_value = ?");
        }
        if (model.getBool() != null) {
            where.add("bool_value = ?");
        }

        String cond = "";
        if (!where.isEmpty()) {
            cond = " WHERE " + String.join(" AND ", where);
        }
        String stmtString = "SELECT * FROM " + TABLE + cond + ";";

        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(stmtString)) {

            int index = 0;
            if (model.getId() != null) {
                stmt.setLong(++index, model.getId());
            }
            if (model.getText() != null && !model.getText().isEmpty()) {
                stmt.setString(++index, "%" + model.getText().toLowerCase() + "%");
            }
            if (model.getNumInt() != null) {
                stmt.setInt(++index, model.getNumInt());
            }
            if (model.getNumDouble() != null) {
                stmt.setDouble(++index, model.getNumDouble());
            }
            if (model.getBool() != null) {
                stmt.setBoolean(++index, model.getBool());
            }
            try (var result = stmt.executeQuery()) {
                while (result.next()) {
                    models.add(readResult(result));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return models;
    }
}
