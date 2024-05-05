package hu.inf.szte.adventure.data;

import hu.inf.szte.adventure.model.User;
import lombok.NonNull;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

import static org.jooq.impl.DSL.*;

public class UserJooqDao implements Dao<Long, User> {

    private final static String TABLE = "usr";
    private final DataSource dataSource;
    private final SQLDialect sqlDialect;

    public UserJooqDao(DataSource ds, SQLDialect dialect) {
        dataSource = ds;
        sqlDialect = dialect;
    }

    private User readRecord(Record record) {
        return new User(
                record.get("id", Long.class),
                record.get("username", String.class),
                record.get("email", String.class),
                record.get("password", String.class));
    }

    @Override
    public void save(User model) {
        var columns = List.of(
                field(name("username")),
                field(name("email")),
                field(name("password")));
        var values = List.of(
                model.getUsername(),
                model.getEmail(),
                model.getPassword());

        var id = DSL.using(dataSource, sqlDialect)
                .insertInto(table(name(TABLE)))
                .columns(columns)
                .values(values)
                .returningResult(field(name("id")))
                .fetchOne(field(name("id")), Long.class);
        model.setId(id);
    }

    @Override
    public Optional<User> findById(Long id) {
        assert id != null;
        return Optional.ofNullable(DSL.using(dataSource, sqlDialect)
                .select()
                .from(TABLE)
                .where(field(name("id")).eq(id))
                .fetchOne(this::readRecord));
    }

    public Optional<User> findByUsername(String username) {
        assert username != null;
        return Optional.ofNullable(DSL.using(dataSource, sqlDialect)
                .select()
                .from(TABLE)
                .where(field(name("username")).eq(username))
                .fetchOne(this::readRecord));
    }

    @Override
    public Iterable<User> findAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterable<User> findAllByIds(Iterable<Long> ids) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterable<User> findAllByCrit(@NonNull User model) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int deleteById(Long id) {
        return 0;
    }

    @Override
    public int deleteAllByIds(Iterable<Long> ids) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int updateById(Long id, User model) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int updateAllByIds(Iterable<Long> ids, User model) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int prune() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int count() {
        return DSL.using(dataSource, sqlDialect)
                .fetchCount(table(name(TABLE)));
    }
}
