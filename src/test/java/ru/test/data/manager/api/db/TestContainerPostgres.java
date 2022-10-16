package ru.test.data.manager.api.db;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

@Service
public class TestContainerPostgres implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Container
    public static PostgreSQLContainer postgres = (PostgreSQLContainer) new PostgreSQLContainer("postgres:alpine")
            .withDatabaseName("postgresTest")
            .withUsername("postgresTest")
            .withPassword("postgresTest")
            .withInitScript("db.sql");

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        postgres.start();
        System.out.println("УРЛ " + postgres.getJdbcUrl());
        TestPropertyValues.of(
                "spring.datasource.url=" + postgres.getJdbcUrl(),
                "spring.datasource.username=" + postgres.getUsername(),
                "spring.datasource.password=" + postgres.getPassword()
        ).applyTo(applicationContext);
        System.out.println(applicationContext.getEnvironment());
    }
}