package ru.test.data.manager.api.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@ComponentScan({"ru.test.data.manager.api"})
@EnableJpaRepositories("ru.test.data.manager.api.repository")
@EntityScan("ru.test.data.manager.api.entity")
@EnableSwagger2

public class DataManagerApplication {
    public static void main(String[] args) {
        SpringApplication.run(DataManagerApplication.class, args);
    }
}

