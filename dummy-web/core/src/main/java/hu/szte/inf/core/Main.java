package hu.szte.inf.core;

import hu.szte.inf.core.util.db.SqlDbSupport;
import org.sqlite.SQLiteDataSource;

public class Main {

    public static void main(String[] args) {

        if (args.length > 0) {

            String mode = args[0];

            if (mode.equals("dbcreate")) {
                createInitialDb();
            }
        }
        else {
            System.out.println("Too few arguments for program.");
            System.out.println("Acceptable arguments:");
            System.out.println("  dbcreate - creates an initial db file named: main.db");
        }
    }
    public static void createInitialDb() {
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

        var ds = new SQLiteDataSource();
        ds.setUrl("jdbc:sqlite:file:main.db");
        var db = new SqlDbSupport(ds);
        db.executeSqlStrings(sql);
    }
}
