package hu.inf.szte.adventure.data;

import hu.inf.szte.adventure.model.Trip;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import javax.sql.DataSource;
import java.util.*;
import java.util.stream.StreamSupport;

import static org.jooq.impl.DSL.*;

public class TripJooqDao implements Dao<Long, Trip> {

    private final static String TABLE = "trip";
    private final DataSource dataSource;
    private final SQLDialect sqlDialect;

    public TripJooqDao(DataSource ds, SQLDialect dialect) {
        dataSource = ds;
        sqlDialect = dialect;
    }

    private Trip readRecord(Record record) {
        return new Trip(
                record.get("id", Long.class),
                record.get("name", String.class),
                record.get("half_board", Boolean.class),
                record.get("num_guests", Integer.class),
                record.get("num_nights", Integer.class),
                record.get("description", String.class));
    }

    private Map<Field<?>, Object> getSetValues(Trip model) {
        var set = new HashMap<Field<?>, Object>();
        if (model.getName() != null) {
            set.put(field(name("name")), model.getName());
        }
        if (model.getHalfBoard() != null) {
            set.put(field(name("half_board")), model.getHalfBoard());
        }
        if (model.getNumGuests() != null) {
            set.put(field(name("num_guests")), model.getNumGuests());
        }
        if (model.getNumNights() != null) {
            set.put(field(name("num_nights")), model.getNumNights());
        }
        if (model.getDescription() != null) {
            set.put(field(name("description")), model.getDescription());
        }
        return set;
    }

    @Override
    public void save(Trip model) {
        assert model != null;
        var columns = List.of(
                field(name("name")),
                field(name("half_board")),
                field(name("num_guests")),
                field(name("num_nights")),
                field(name("description")));
        var values = List.of(
                model.getName(),
                model.getHalfBoard(),
                model.getNumGuests(),
                model.getNumNights(),
                model.getDescription());

        var id = DSL.using(dataSource, sqlDialect)
                .insertInto(table(name(TABLE)))
                .columns(columns)
                .values(values)
                .returningResult(field(name("id")))
                .fetchOne(field(name("id")), Long.class);
        model.setId(id);
    }

    @Override
    public Optional<Trip> findById(Long id) {
        assert id != null;
        return Optional.ofNullable(DSL.using(dataSource, sqlDialect)
                .select()
                .from(TABLE)
                .where(field(name("id")).eq(id))
                .fetchOne(this::readRecord));
    }

    @Override
    public Iterable<Trip> findAll() {
        return DSL.using(dataSource, sqlDialect)
                .select()
                .from(TABLE)
                .fetch(this::readRecord);
    }

    @Override
    public Iterable<Trip> findAllByIds(Iterable<Long> ids) {
        assert ids != null;
        var where = StreamSupport.stream(ids.spliterator(), false)
                .map(id -> field(name("id")).eq(id))
                .toList();
        return DSL.using(dataSource, sqlDialect)
                .select()
                .from(TABLE)
                .where(or(where))
                .fetch(this::readRecord);
    }

    @Override
    public Iterable<Trip> findAllByCrit(Trip model) {
        assert model != null;
        var where = new ArrayList<Condition>();

        if (model.getId() != null) {
            where.add(field(name("id")).eq(model.getId()));
        }
        if (model.getName() != null) {
            where.add(field(name("name")).likeIgnoreCase("%" + model.getName().replace("%", "\\%") + "%").escape('\\'));
        }
        if (model.getHalfBoard() != null) {
            where.add(field(name("half_board")).eq(model.getHalfBoard()));
        }
        if (model.getNumGuests() != null) {
            where.add(field(name("num_guests")).eq(model.getNumGuests()));
        }
        if (model.getNumNights() != null) {
            where.add(field(name("num_nights")).eq(model.getNumNights()));
        }
        if (model.getDescription() != null) {
            where.add(field(name("description")).likeIgnoreCase("%" + model.getDescription().replace("%", "\\%") + "%").escape('\\'));
        }

        return DSL.using(dataSource, sqlDialect)
                .select()
                .from(TABLE)
                .where(and(where))
                .fetch(this::readRecord);
    }

    @Override
    public int deleteById(Long id) {
        assert id != null;
        return DSL.using(dataSource, sqlDialect)
                .deleteFrom(table(name(TABLE)))
                .where(field(name("id")).eq(id))
                .execute();
    }

    @Override
    public int deleteAllByIds(Iterable<Long> ids) {
        assert ids != null;
        var where = StreamSupport.stream(ids.spliterator(), false)
                .map(id -> field(name("id")).eq(id))
                .toList();
        return DSL.using(dataSource, sqlDialect)
                .deleteFrom(table(name(TABLE)))
                .where(or(where))
                .execute();
    }

    @Override
    public int updateById(Long id, Trip model) {
        assert id != null;

        var set = getSetValues(model);
        if (set.isEmpty()) {
            return 0;
        }
        return DSL.using(dataSource, sqlDialect)
                .update(table(name(TABLE)))
                .set(set)
                .where(field(name("id")).eq(id))
                .execute();
    }

    @Override
    public int updateAllByIds(Iterable<Long> ids, Trip model) {
        assert ids != null;

        var set = getSetValues(model);
        if (set.isEmpty()) {
            return 0;
        }

        var where = StreamSupport.stream(ids.spliterator(), false)
                .map(id -> field(name("id")).eq(id))
                .toList();
        // By design choice if there are no conditions, we update all records
        if (where.isEmpty()) {
            where = List.of(noCondition());
        }

        return DSL.using(dataSource, sqlDialect)
                .update(table(name(TABLE)))
                .set(set)
                .where(or(where))
                .execute();
    }

    @Override
    public int prune() {
        return DSL.using(dataSource, sqlDialect)
                .deleteFrom(table(name(TABLE)))
                .execute();
    }

    @Override
    public int count() {
        return DSL.using(dataSource, sqlDialect)
                .fetchCount(table(name(TABLE)));
    }
}
