package hello.sse.springssedemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SpringSseDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringSseDemoApplication.class, args);
    }

}
