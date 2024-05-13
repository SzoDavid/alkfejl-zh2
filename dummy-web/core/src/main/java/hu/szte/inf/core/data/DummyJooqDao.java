package hu.szte.inf.core.data;

import hu.szte.inf.core.model.Dummy;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import javax.sql.DataSource;
import java.util.*;
import java.util.stream.StreamSupport;

import static org.jooq.impl.DSL.*;

/**
 * You should not be here. Thread lightly...
 */
public class DummyJooqDao implements Dao<Long, Dummy> {

    private final DataSource dataSource;
    private final SQLDialect sqlDialect;

    public DummyJooqDao(DataSource ds, SQLDialect dialect) {
        dataSource = ds;
        sqlDialect = dialect;
    }

    private Dummy readRecord(Record record) {
        return new Dummy(
                record.get(Dummy.ID, Long.class),
                record.get(Dummy.SOME_STRING, String.class),
                record.get(Dummy.ANOTHER_STRING, String.class),
                record.get(Dummy.SOME_BOOL, Boolean.class),
                record.get(Dummy.SOME_INT, Integer.class),
                record.get(Dummy.OTHER_INT, Integer.class));
    }

    private Map<Field<?>, Object> getSetValues(Dummy model) {
        var set = new HashMap<Field<?>, Object>();
        if (model.getId() != null) {
            set.put(field(name(Dummy.ID)), model.getId());
        }
        if (model.getSomeString() != null) {
            set.put(field(name(Dummy.SOME_STRING)), model.getSomeString());
        }
        if (model.getAnotherString() != null) {
            set.put(field(name(Dummy.ANOTHER_STRING)), model.getAnotherString());
        }
        if (model.getSomeBool() != null) {
            set.put(field(name(Dummy.SOME_BOOL)), model.getSomeBool());
        }
        if (model.getSomeInt() != null) {
            set.put(field(name(Dummy.SOME_INT)), model.getSomeInt());
        }
        if (model.getOtherInt() != null) {
            set.put(field(name(Dummy.OTHER_INT)), model.getOtherInt());
        }
        return set;
    }

    @Override
    public void save(Dummy model) {
        assert model != null;
        var columns = new ArrayList<>(List.of(
                field(name(Dummy.SOME_STRING)),
                field(name(Dummy.ANOTHER_STRING)),
                field(name(Dummy.SOME_BOOL)),
                field(name(Dummy.SOME_INT)),
                field(name(Dummy.OTHER_INT))));
        var values = new ArrayList<>(List.of(
                model.getSomeString(),
                model.getAnotherString(),
                model.getSomeBool(),
                model.getSomeInt(),
                model.getOtherInt()));
        if (model.getId() != null) {
            columns.add(0, field(name(Dummy.ID)));
            values.add(0, model.getId());
        }

        var id = DSL.using(dataSource, sqlDialect)
                .insertInto(table(name(Dummy.TABLE)))
                .columns(columns)
                .values(values)
                .returningResult(field(name(Dummy.ID)))
                .fetchOne(field(name(Dummy.ID)), Long.class);
        model.setId(id);
    }

    @Override
    public Optional<Dummy> findById(Long id) {
        assert id != null;
        return Optional.ofNullable(DSL.using(dataSource, sqlDialect)
                .select()
                .from(Dummy.TABLE)
                .where(field(name(Dummy.ID)).eq(id))
                .fetchOne(this::readRecord));
    }

    @Override
    public Iterable<Dummy> findAll() {
        return DSL.using(dataSource, sqlDialect)
                .select()
                .from(Dummy.TABLE)
                .fetch(this::readRecord);
    }

    @Override
    public Iterable<Dummy> findAllByIds(Iterable<Long> ids) {
        assert ids != null;
        var where = StreamSupport.stream(ids.spliterator(), false)
                .map(id -> field(name(Dummy.ID)).eq(id))
                .toList();
        return DSL.using(dataSource, sqlDialect)
                .select()
                .from(Dummy.TABLE)
                .where(or(where))
                .fetch(this::readRecord);
    }

    @Override
    public Iterable<Dummy> findAllByCrit(Dummy model) {
        assert model != null;
        var where = new ArrayList<Condition>();

        if (model.getId() != null) {
            where.add(field(name(Dummy.ID)).eq(model.getId()));
        }
        if (model.getSomeString() != null) {
            where.add(field(name(Dummy.SOME_STRING)).likeIgnoreCase("%" + model.getSomeString().replace("%", "\\%") + "%").escape('\\'));
        }
        if (model.getAnotherString() != null) {
            where.add(field(name(Dummy.ANOTHER_STRING)).likeIgnoreCase("%" + model.getAnotherString().replace("%", "\\%") + "%").escape('\\'));
        }
        if (model.getSomeBool() != null) {
            where.add(field(name(Dummy.SOME_BOOL)).eq(model.getSomeBool()));
        }
        if (model.getSomeInt() != null) {
            where.add(field(name(Dummy.SOME_INT)).eq(model.getSomeInt()));
        }
        if (model.getOtherInt() != null) {
            where.add(field(name(Dummy.OTHER_INT)).eq(model.getOtherInt()));
        }

        return DSL.using(dataSource, sqlDialect)
                .select()
                .from(Dummy.TABLE)
                .where(and(where))
                .fetch(this::readRecord);
    }

    @Override
    public int deleteById(Long id) {
        assert id != null;
        return DSL.using(dataSource, sqlDialect)
                .deleteFrom(table(name(Dummy.TABLE)))
                .where(field(name(Dummy.ID)).eq(id))
                .execute();
    }

    @Override
    public int deleteAllByIds(Iterable<Long> ids) {
        assert ids != null;
        var where = StreamSupport.stream(ids.spliterator(), false)
                .map(id -> field(name(Dummy.ID)).eq(id))
                .toList();
        return DSL.using(dataSource, sqlDialect)
                .deleteFrom(table(name(Dummy.TABLE)))
                .where(or(where))
                .execute();
    }

    @Override
    public int updateById(Long id, Dummy model) {
        assert id != null;

        var set = getSetValues(model);
        if (set.isEmpty()) {
            return 0;
        }
        return DSL.using(dataSource, sqlDialect)
                .update(table(name(Dummy.TABLE)))
                .set(set)
                .where(field(name(Dummy.ID)).eq(id))
                .execute();
    }

    @Override
    public int updateAllByIds(Iterable<Long> ids, Dummy model) {
        assert ids != null;

        var set = getSetValues(model);
        if (set.isEmpty()) {
            return 0;
        }

        var where = StreamSupport.stream(ids.spliterator(), false)
                .map(id -> field(name(Dummy.ID)).eq(id))
                .toList();
        // By design choice if there are no conditions, we update all records
        if (where.isEmpty()) {
            where = List.of(noCondition());
        }

        return DSL.using(dataSource, sqlDialect)
                .update(table(name(Dummy.TABLE)))
                .set(set)
                .where(or(where))
                .execute();
    }

    @Override
    public int prune() {
        return DSL.using(dataSource, sqlDialect)
                .deleteFrom(table(name(Dummy.TABLE))).execute();
    }

    @Override
    public int count() {
        return DSL.using(dataSource, sqlDialect)
                .fetchCount(table(name(Dummy.TABLE)));
    }
}
