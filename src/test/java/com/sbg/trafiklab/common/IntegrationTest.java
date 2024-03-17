package com.sbg.trafiklab.common;


import com.sbg.trafiklab.util.DatabaseTestUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class IntegrationTest {

    static {
        var redisPort = 6379;
        var redis =
                new GenericContainer<>(DockerImageName.parse("redis:5.0.3-alpine")).withExposedPorts(redisPort);
        redis.start();
        System.setProperty("spring.data.redis.host", redis.getHost());
        System.setProperty("spring.data.redis.port", redis.getMappedPort(redisPort).toString());

        System.setProperty("trafiklab.data.fetch-from-api", Boolean.FALSE.toString());
        System.setProperty(" trafiklab.data.sync-on-start", Boolean.FALSE.toString());
    }

    @Autowired
    private DatabaseTestUtil databaseTestUtil;


    @AfterEach
    void afterEach() {
        databaseTestUtil.clearDatabase();
    }

}
