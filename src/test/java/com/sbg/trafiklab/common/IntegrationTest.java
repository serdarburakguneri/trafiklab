package com.sbg.trafiklab.common;

import java.util.List;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class IntegrationTest {

    private static final String POSTGRES_DATABASE_NAME = "testdb";
    private static final String POSTGRES_USERNAME = "test";
    private static final String POSTGRES_PASSWORD = "test";
    private static final String POSTGRES_HOST_PORT = "5442";
    private static final String POSTGRES_CONTAINER_PORT = "5432";

    private static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:latest")
                    .withDatabaseName(POSTGRES_DATABASE_NAME)
                    .withUsername(POSTGRES_USERNAME)
                    .withPassword(POSTGRES_PASSWORD)
                    .withExposedPorts(Integer.valueOf(POSTGRES_HOST_PORT));

    @BeforeAll
    void setUp() {
        postgreSQLContainer.setPortBindings(List.of("%s:%s".formatted(POSTGRES_HOST_PORT, POSTGRES_CONTAINER_PORT)));
        postgreSQLContainer.start();
        migrateDatabase();
    }

    void migrateDatabase() {
        var flyway = Flyway.configure()
                .dataSource(postgreSQLContainer.getJdbcUrl(), POSTGRES_PASSWORD, POSTGRES_PASSWORD)
                .load();
        flyway.migrate();
    }

    @AfterAll
    void tearDown() {
        postgreSQLContainer.stop();
    }

    @DynamicPropertySource
    static void r2dbcProperties(DynamicPropertyRegistry registry) {
        String r2dbcUrl = String.format("r2dbc:postgresql://%s:%s/%s",
                postgreSQLContainer.getHost(),
                POSTGRES_HOST_PORT,
                POSTGRES_DATABASE_NAME);

        registry.add("spring.r2dbc.url", () -> r2dbcUrl);
        registry.add("spring.r2dbc.username", () -> POSTGRES_USERNAME);
        registry.add("spring.r2dbc.password", () -> POSTGRES_PASSWORD);

        registry.add("spring.flyway.baseline-on-migrate", () -> false);
    }
}
