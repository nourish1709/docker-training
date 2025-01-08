package com.nourish1709.learning.infrastructuretests;

import lombok.SneakyThrows;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcConnectionDetails;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.config.client.ConfigClientAutoConfiguration;
import org.springframework.cloud.config.client.ConfigClientProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.web.client.RestTemplate;

import java.sql.Connection;
import java.sql.DriverManager;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
class ShoppingServiceInfrastructureTest {

    @Order(1)
    @Nested
    @SpringBootTest(classes = ConfigClientAutoConfiguration.class)
    class ConfigServerConnectionTest {

        @Test
        @DisplayName("config-service is available on localhost:8888")
        void configServiceIsAvailable(@Autowired ConfigClientProperties properties) {
            HttpHeaders headers = getHttpHeaders(properties);
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            String uri = properties.getUri()[0];
            String path = "/{name}/{profile}";
            Object[] args = new String[]{properties.getName(), properties.getProfile()};

            assertDoesNotThrow(() -> new RestTemplate().exchange(uri + path, HttpMethod.GET, entity, Object.class, args),
                    "Could not connect to config-server. Make sure it's available via " + uri);
        }

        private HttpHeaders getHttpHeaders(ConfigClientProperties properties) {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(singletonList(MediaType.parseMediaType(properties.getMediaType())));
            headers.setAcceptCharset(singletonList(properties.getCharset()));
            return headers;
        }
    }

    @Order(2)
    @Nested
    @SpringBootTest(classes = DataSourceAutoConfiguration.class)
    class DatabaseConnectionTest {

        @SneakyThrows
        @Test
        @DisplayName("shopping database is available on localhost:5432")
        void databaseIsAvailable(@Autowired JdbcConnectionDetails connectionDetails) {
            try (Connection ignored = assertDoesNotThrow(() -> DriverManager.getConnection(connectionDetails.getJdbcUrl(), connectionDetails.getUsername(), connectionDetails.getPassword()),
                    "Could not connect to Postgres database. Make sure it's available on localhost:5432.")) {
                // Testing if connection can be established
            }
        }
    }

    @Order(3)
    @Nested
    @SpringBootTest(classes = KafkaAutoConfiguration.class)
    class KafkaConnectionTest {

        @Test
        @DisplayName("Kafka is available on localhost:9092")
        void kafkaIsAvailable(@Autowired KafkaAdmin kafkaAdmin) {
            String clusterId = assertDoesNotThrow(kafkaAdmin::clusterId,
                    "Could not connect to Kafka. Make sure it's available on localhost:9092.");

            assertNotNull(clusterId, "Kafka cluster ID is null. Make sure Kafka is available on localhost:9092.");
        }
    }
}
