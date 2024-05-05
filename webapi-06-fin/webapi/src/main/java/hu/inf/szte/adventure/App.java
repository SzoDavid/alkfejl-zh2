package hu.inf.szte.adventure;

import com.google.gson.Gson;
import hu.inf.szte.adventure.model.Sight;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

import java.math.BigDecimal;

@SpringBootApplication
@ServletComponentScan
public class App {

    public static void main(String[] args) {
        var sight = new Sight(2L, "dsa asd", BigDecimal.valueOf(42.24), 10, 18, "...", 8);
        String json = new Gson().toJson(sight);

        var parsed = new Gson().fromJson(json, Sight.class);

        new SpringApplication(App.class).run(args);
    }
}
