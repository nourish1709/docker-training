package com.nourish1709.learning.infrastructuretests;

import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.core.KafkaAdmin;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
class ConfigServiceInfrastructureTest {

    @Order(1)
    @Nested
    @SpringBootTest(classes = RedisAutoConfiguration.class)
    class RedisConnectionTest {

        @Test
        @DisplayName("Redis is available on localhost:6379")
        void redisIsAvailable(@Autowired StringRedisTemplate redisTemplate) {
            System.out.println(redisTemplate);

            assertDoesNotThrow(redisTemplate::getClientList,
                    "Could not connect to Redis. Make sure it's available on localhost:6379.");
        }

    }

    @Order(2)
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
