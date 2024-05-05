package hu.inf.szte.adventure.util.fx;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.StreamSupport;

/**
 * Should provide help with {@link TableView} objects.
 * Helps with creating a schema for a given table view, e.g.:
 * tableSupport.createSchema();
 *
 * @param clazz Class to be loaded as rows of this table
 * @param table The table to modify/prepare
 * @param <T> Should be picked up automatically, the type parameter for clazz, and table
 */
public record TableSupport<T>(Class<T> clazz, TableView<T> table) {

    public static <T> TableSupport<T> from(Class<T> clazz, TableView<T> table) {
        return new TableSupport<>(clazz, table);
    }

    /**
     * Creates schema for the given table.
     * Columns will be added on demanded given by the clazz parameter,
     *
     * @param formatter Function used for string formatting,
     *                  should have exactly one {@link String} as a parameter,
     *                  and produce the formatted result.
     * @param ignoreList If any of the class' fields should not be displayed,
     *                   should be specified in the ignoreList, e.g.: List.of("id")
     */
    public void createSchema(Function<String, String> formatter, Iterable<String> ignoreList) {
        table.getColumns().clear();
        var cache = StreamSupport.stream(ignoreList.spliterator(), false).toList();

        var columns = Arrays.stream(clazz.getDeclaredFields())
                // filter any ignored element
                .filter(field -> !cache.contains(field.getName()))
                // map declared fields to table columns
                .map(m -> {
                    // create the table column with a name and value
                    // as given by the current field
                    TableColumn<T, ?> col = new TableColumn<>(formatter.apply(m.getName()));
                    col.setCellValueFactory(new PropertyValueFactory<>(m.getName()));
                    return col;
                })
                .toList();
        table.getColumns().addAll(columns);
    }

    /**
     * See {@link TableSupport#createSchema(Function, Iterable)}
     * Call is made with a non-formatting string function, and an empty ignore list.
     */
    public void createSchema() {
        Function<String, String> formatter = (String s) -> s;
        var ignoreList = new ArrayList<String>();
        createSchema(formatter, ignoreList);
    }

    /**
     * See {@link TableSupport#createSchema(Function, Iterable)}
     * Call is made using an empty ignore list.
     *
     * @param formatter String formatter function
     */
    public void createSchema(Function<String, String> formatter) {
        var ignoreList = new ArrayList<String>();
        createSchema(formatter, ignoreList);
    }

    /**
     * See {@link TableSupport#createSchema(Function, Iterable)}
     * Call is made using a non-formatting string function.
     *
     * @param ignoreList Ignore list of class fields
     */
    public void createSchema(Iterable<String> ignoreList) {
        Function<String, String> formatter = (String s) -> s;
        createSchema(formatter, ignoreList);
    }
}
