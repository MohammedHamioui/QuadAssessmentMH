package Quad.Assesment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    // injecteer RestTemplate zodat we hem kunnen mocken in tests (DI)
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}