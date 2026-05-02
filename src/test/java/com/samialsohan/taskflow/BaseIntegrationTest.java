package com.samialsohan.taskflow;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.postgresql.PostgreSQLContainer;

import org.springframework.context.annotation.Import;

@Import(BaseIntegrationTest.TestcontainersConfiguration.class)
public abstract class BaseIntegrationTest {

    @TestConfiguration(proxyBeanMethods = false)
    static class TestcontainersConfiguration {

        @Bean
        @ServiceConnection
        PostgreSQLContainer postgresContainer() {
            return new PostgreSQLContainer("postgres:16-alpine")
                    .withDatabaseName("taskflow_test")
                    .withUsername("test")
                    .withPassword("test");
        }
    }
}