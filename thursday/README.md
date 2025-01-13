# Welcome to the Docker Compose Learning Exercise

This exercise will help you understand the basics of Docker Compose by working with a simple architecture that consists
of two Spring Boot applications:

1. **Config Service**: A Spring Cloud Config Server that stores properties in Redis and publishes updates to Kafka.
2. **Shopping Service**: A Spring Cloud Config Client and Spring Web MVC application that interacts with a Postgres
   database to manages shopping items.

The architecture includes additional services such as Redis, Postgres, Kafka, all orchestrated using Docker Compose.

## Architecture Overview

### Components:

- **Redis**: Stores configuration properties for the Config Service.
- **Postgres**: Database for the Shopping Service.
- **Kafka**: Handles event communication through the `springCloudBus` topic.
- **Zookeeper**: Manages Kafka cluster coordination.
- **Config Service**: Fetches and serves configuration properties.
- **Shopping Service**: Fetches properties from the Config Service and interacts with Postgres.
- **Functional Tests**: Verifies integration and functionality of the services.

### Prerequisites

- If you're using Intellij IDEA, make sure to configure the following Maven projects:
    - thursday (`./thursday/pom.xml`)
    - config-service (`./thursday/config-service/pom.xml`)
    - shopping-service (`./thursday/shopping-service/pom.xml`)
    - functional-tests (`./thursday/functional-tests/pom.xml`)
    - infrastructure-tests (`./thursday/infrastructure-tests/pom.xml`)

## Tasks

### Task 0: Basic Setup

**Objective:** Configure essential services for the Config Service: Redis, Zookeeper, and Kafka.

**File to update:** `docker-compose-0.yml`

**Requirements:**

- Redis:
    - Use the `redis:7.4.1` image.
    - Expose port `6379` on the host machine.
- Zookeeper:
    - Use the `confluentinc/cp-zookeeper:7.8.0` image.
    - Configure the following environment variables:
        - `ZOOKEEPER_CLIENT_PORT: 2181`
        - `ZOOKEEPER_TICK_TIME: 2000`
- Kafka:
    - Use the `confluentinc/cp-kafka:7.8.0` image.
    - Expose port `9092` on the host machine.
    - Depend on Zookeeper.
    - Configure the following environment variables:
        - `KAFKA_BROKER_ID: 1`
        - `KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181`
        - `KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://localhost:29092,PLAINTEXT_HOST://localhost:9092`
        - `KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT,PLAINTEXT_HOST:PLAINTEXT`
        - `KAFKA_INTER_BROKER_LISTENER_NAME: PLAINTEXT`
        - `KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1`

**Testing:**

- Run `docker-compose --file docker-compose-0.yml up --detach`.
- Execute `com.nourish1709.learning.infrastructuretests.ConfigServiceInfrastructureTest`.

### Task 1: Add Database and Enhance Configuration

**Objective:** Configure essential services for both Config Client and Config Service: Postgres, Redis, Zookeeper, and
Kafka.

**File to update:** `docker-compose-1.yml`

**Requirements:**

- Postgres:
    - Use the `postgres:17.2` image.
    - Expose port `5432` on the host machine.
    - Set the following environment variables:
        - `POSTGRES_HOST_AUTH_METHOD: trust`. It allows anyone to connect to the database.
        - `POSTGRES_USER: student`
        - `POSTGRES_DB: shopping`
- Redis:
    - Same configuration as in Task 0.
- Kafka:
    - Same configuration as in Task 0.
- Zookeeper:
    - Same configuration as in Task 0.

**Testing:**

- Run `docker-compose --file docker-compose-1.yml up --detach`.
- Run the Config Service (`com.nourish1709.learning.configservice.ConfigServiceApplication`).
- Execute the following tests:
    - `com.nourish1709.learning.infrastructuretests.ConfigServiceInfrastructureTest`.
    - `com.nourish1709.learning.infrastructuretests.ShoppingServiceInfrastructureTest`.
- Run the Shopping Service with `host` profile (`com.nourish1709.learning.shoppingservice.ShoppingServiceApplication`)
- Execute the following tests:
    - `com.nourish1709.learning.functionaltests.FunctionalTests`

### Task 2: Full Application Setup

**Objective:** Containerize Config and Shopping services and integrate all components.

**File to update:** `docker-compose-2.yml`

**Requirements:**

- **Postgres**:
    - Use the `postgres:17.2` image.
    - `POSTGRES_HOST_AUTH_METHOD` should be set to `password` to require username/password from clients.
    - Create `shopping` database.
    - Credentials should be sourced from `./infrastructure/db/user.txt` and `./infrastructure/db/password.txt`.
    - Isolate in its own network to ensure only the Shopping Service can connect to it via `db:5432` url.
- **Redis**:
    - Use the `redis:7.4.1` image.
    - Isolate in its own network to ensure only the Config Service can connect to it via `redis:6379`.
- **Zookeeper**:
    - Same configuration as in Task 0.
    - Should be accessible by Kafka
- **Kafka**:
    - Use the `confluentinc/cp-kafka:7.8.0` image.
    - Expose port `9092` on the host machine.
    - Depend on Zookeeper.
    - Configure the following environment variables (**Note that `KAFKA_ADVERTISED_LISTENERS` value is different**):
        - `KAFKA_BROKER_ID: 1`
        - `KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181`
        - `KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://kafka:9092,PLAINTEXT_HOST://localhost:29092`
        - `KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT,PLAINTEXT_HOST:PLAINTEXT`
        - `KAFKA_INTER_BROKER_LISTENER_NAME: PLAINTEXT`
        - `KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1`
    - Ensure both Config Service and Shopping Service can connect to it via `kafka:9092`.
- **Config Service**:
    - Finish
    - Build using `./config-service` Dockerfile.
    - Depend on Kafka and Redis.
    - Should be run with profile `container` (use `SPRING_PROFILES_ACTIVE` environment variable)
    - Connects to Kafka on `kafka:9092`.
    - Connects to Redis on `redis:6379`.
- **Shopping Service**:
    - Build using `./shopping-service` Dockerfile.
    - Depend on Config Service, Kafka, and Postgres.
    - Use secrets for database credentials sourced from `./infrastructure/db/user.txt` and
      `./infrastructure/db/password.txt`.
    - Should be run with profile `container` (use `SPRING_PROFILES_ACTIVE` environment variable)
    - Connects to Kafka on `kafka:9092`.
    - Connects to Postgres on `db:5432`.
    - Connects to the Config Service on `config-service:8080`
- **Functional Tests** (_Verifies the full system functionality_):
    - Use `maven:3.9.9-amazoncorretto-21-alpine` image.
    - Ensure both Config Service and Shopping Service are reachable from this container.
    - Depends on the Config Service and Shopping Service.
    - Bind mount the content of the `thursday` directory into `/usr/src/mymaven`
    - Set working directory as `/usr/src/mymaven`
    - Run command `mvn clean test -pl :functional-tests -Dspring.profiles.active=container`

**Testing:**

- Run `docker-compose --file docker-compose-2.yml up --detach`.
- Open the logs of the functional-tests container. You should see a message about a successful build.

---

### Resources

- Docker Compose [documentation](https://docs.docker.com/compose/)
- Postgres image [documentation](https://hub.docker.com/_/postgres)
- Redis image [documentation](https://hub.docker.com/_/redis)
- Kafka [documentation](https://kafka.apache.org/documentation)

---

Enjoy practicing with Docker Compose!
