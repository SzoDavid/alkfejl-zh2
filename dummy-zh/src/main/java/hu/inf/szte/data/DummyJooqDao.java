package hu.inf.szte.data;

import hu.inf.szte.model.Dummy;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.jooq.Condition;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

import static org.jooq.impl.DSL.*;

@AllArgsConstructor
public class DummyJooqDao implements Dao<Dummy> {

    private static final String TABLE = "dummy";

    private final DataSource dataSource;
    private final SQLDialect sqlDialect;

    @Override
    public void save(@NonNull Dummy model) {
        var columns = List.of(field(name("text_value")),
                field(name("integer_value")),
                field(name("double_value")),
                field(name("bool_value")));
        var values = List.of(model.getText(),
                model.getNumInt(),
                model.getNumDouble(),
                model.getBool());
        var id = DSL.using(dataSource, sqlDialect)
                .insertInto(table(name(TABLE)))
                .columns(columns)
                .values(values)
                .returningResult(field(name("id")))
                .fetchOne("id", Long.class);
        assert id != null && id > 0;
        model.setId(id);
    }

    @Override
    public List<Dummy> findAll() {
        return DSL.using(dataSource, sqlDialect)
                .select()
                .from(table(name(TABLE)))
                .fetch(r -> new Dummy(r.get("id", Long.class),
                        r.get("text_value", String.class),
                        r.get("integer_value", Integer.class),
                        r.get("double_value", Double.class),
                        r.get("bool_value", Boolean.class)));
    }

    @Override
    public List<Dummy> findAllByCrit(@NonNull Dummy model) {
        var where = new ArrayList<Condition>();
        if (model.getId() != null) {
            where.add(field(name("id")).eq(model.getId()));
        }
        if (model.getText() != null && !model.getText().isEmpty()) {
            where.add(field(name("text_value")).likeIgnoreCase("%" + model.getText() + "%"));
        }
        if (model.getNumInt() != null) {
            where.add(field(name("integer_value")).eq(model.getNumInt()));
        }
        if (model.getNumDouble() != null) {
            where.add(field(name("double_value")).eq(model.getNumDouble()));
        }
        if (model.getBool() != null) {
            where.add(field(name("bool_value")).eq(model.getBool()));
        }
        return DSL.using(dataSource, sqlDialect)
                .select()
                .from(table(name(TABLE)))
                .where(and(where))
                .fetch(r -> new Dummy(r.get("id", Long.class),
                        r.get("text_value", String.class),
                        r.get("integer_value", Integer.class),
                        r.get("double_value", Double.class),
                        r.get("bool_value", Boolean.class)));
    }
}
