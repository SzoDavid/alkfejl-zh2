package hu.inf.szte.adventure.data;

import hu.inf.szte.adventure.model.Sight;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.StreamSupport;

import static org.jooq.impl.DSL.*;

public class SightJooqDao implements Dao<Long, Sight> {

    private final static String TABLE = "sight";
    private final DataSource dataSource;
    private final SQLDialect sqlDialect;

    public SightJooqDao(DataSource ds, SQLDialect dialect) {
        dataSource = ds;
        sqlDialect = dialect;
    }

    private Sight readRecord(Record record) {
        return new Sight(
                record.get("id", Long.class),
                record.get("name", String.class),
                record.get("price", BigDecimal.class),
                record.get("opening", Integer.class),
                record.get("closing", Integer.class),
                record.get("description", String.class),
                record.get("popularity", Integer.class));
    }

    private Map<Field<?>, Object> getSetValues(Sight model) {
        var set = new HashMap<Field<?>, Object>();
        if (model.getName() != null) {
            set.put(field(name("name")), model.getName());
        }
        if (model.getPrice() != null) {
            set.put(field(name("price")), model.getPrice());
        }
        if (model.getOpeningHour() != null) {
            set.put(field(name("opening")), model.getOpeningHour());
        }
        if (model.getClosingHour() != null) {
            set.put(field(name("closing")), model.getClosingHour());
        }
        if (model.getDescription() != null) {
            set.put(field(name("description")), model.getDescription());
        }
        if (model.getPopularity() != null) {
            set.put(field(name("popularity")), model.getPopularity());
        }
        return set;
    }

    @Override
    public void save(Sight model) {
        assert model != null;
        var columns = List.of(
                field(name("name")),
                field(name("price")),
                field(name("opening")),
                field(name("closing")),
                field(name("description")),
                field(name("popularity")));
        var values = List.of(
                model.getName(),
                model.getPrice(),
                model.getOpeningHour(),
                model.getClosingHour(),
                model.getDescription(),
                model.getPopularity());

        var id = DSL.using(dataSource, sqlDialect)
                .insertInto(table(name(TABLE)))
                .columns(columns)
                .values(values)
                .returningResult(field(name("id")))
                .fetchOne(field(name("id")), Long.class);
        model.setId(id);
    }

    @Override
    public Optional<Sight> findById(Long id) {
        assert id != null;
        return Optional.ofNullable(DSL.using(dataSource, sqlDialect)
                .select()
                .from(TABLE)
                .where(field(name("id")).eq(id))
                .fetchOne(this::readRecord));
    }

    @Override
    public Iterable<Sight> findAll() {
        return DSL.using(dataSource, sqlDialect)
                .select()
                .from(TABLE)
                .fetch(this::readRecord);
    }

    @Override
    public Iterable<Sight> findAllByIds(Iterable<Long> ids) {
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
    public Iterable<Sight> findAllByCrit(Sight model) {
        assert model != null;
        var where = new ArrayList<Condition>();

        if (model.getId() != null) {
            where.add(field(name("id")).eq(model.getId()));
        }
        if (model.getName() != null) {
            where.add(field(name("name")).likeIgnoreCase("%" + model.getName().replace("%", "\\%") + "%").escape('\\'));
        }
        if (model.getPrice() != null) {
            where.add(field(name("price")).eq(model.getPrice()));
        }
        if (model.getOpeningHour() != null) {
            where.add(field(name("opening")).eq(model.getOpeningHour()));
        }
        if (model.getClosingHour() != null) {
            where.add(field(name("closing")).eq(model.getClosingHour()));
        }
        if (model.getDescription() != null) {
            where.add(field(name("description")).likeIgnoreCase("%" + model.getDescription().replace("%", "\\%") + "%").escape('\\'));
        }
        if (model.getPopularity() != null) {
            where.add(field(name("popularity")).eq(model.getPopularity()));
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
    public int updateById(Long id, Sight model) {
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
    public int updateAllByIds(Iterable<Long> ids, Sight model) {
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
                .deleteFrom(table(name(TABLE))).execute();
    }

    @Override
    public int count() {
        return DSL.using(dataSource, sqlDialect)
                .fetchCount(table(name(TABLE)));
    }
}
