package dankook.cs.aj24;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class Aj24Application {

    public static void main(String[] args) {
        SpringApplication.run(Aj24Application.class, args);
    }

}
