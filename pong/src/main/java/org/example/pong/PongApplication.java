package org.example.pong;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableWebFlux
public class PongApplication {

    public static void main(String[] args) {
        SpringApplication.run(PongApplication.class, args);
    }

}
