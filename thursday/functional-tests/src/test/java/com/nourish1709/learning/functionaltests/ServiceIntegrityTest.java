package com.nourish1709.learning.functionaltests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Profile;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@Profile("container")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ServiceIntegrityTest {

    private static final String URL_FORMAT = "http://%s/actuator/health/%s";
    private static final String PING_URL_FORMAT = "http://%s/actuator/health/ping";

    @Autowired
    private TestRestTemplate restTemplate;

    @Value("${hosts.config-service}")
    private String configServiceHost;
    @Value("${hosts.shopping-service}")
    private String shoppingServiceHost;

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @DisplayName("config-service tests")
    class ConfigServiceIntegrityTest {

        @Test
        @Order(1)
        @DisplayName("config-service is available")
        void configServiceIsAvailable() {
            assertDoesNotThrow(() -> restTemplate.getForObject(PING_URL_FORMAT.formatted(configServiceHost), Object.class),
                    "Failed to send a request to config-service. Make sure it is on " + configServiceHost);
        }

        @Test
        @Order(2)
        void redisConnectionIsHealth() {

        }

        @Test
        @Order(3)
        @DisplayName("Postgres is not available for config-service")
        void databaseIsNotAvailable() {

        }

        @Test
        @Order(4)
        void kafkaConnectionIsHealthy() {
            System.out.println(restTemplate);

        }
    }

    @Nested
    @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
    class ShoppingServiceIntegrityTest {

    }
}
