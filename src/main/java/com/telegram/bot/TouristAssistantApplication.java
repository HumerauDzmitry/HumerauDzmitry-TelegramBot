package com.telegram.bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
public class TouristAssistantApplication {

    public static void main(String[] args) {
        ApiContextInitializer.init();
        SpringApplication.run(TouristAssistantApplication.class, args);
    }

}
