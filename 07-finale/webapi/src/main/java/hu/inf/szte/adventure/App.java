package hu.inf.szte.adventure;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class App {

    public static void main(String[] args) {
        new SpringApplication(App.class).run(args);
    }
}
