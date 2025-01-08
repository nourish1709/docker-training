package com.nourish1709.learning.functionaltests;

import com.nourish1709.learning.functionaltests.dto.Item;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.time.Duration;
import java.util.List;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FunctionalTests {

    private final List<Item> expectedItemsWithoutDiscount = List.of(
            new Item("item1", "description1", 100.0, 1),
            new Item("item2", "description2", 200.0, 2),
            new Item("item3", "description3", 300.0, 3),
            new Item("item4", "description4", 400.0, 4),
            new Item("item5", "description5", 500.0, 5)
    );
    private final List<Item> expectedItemsWithDiscount = List.of(
            new Item("item1", "description1", 90.0, 1),
            new Item("item2", "description2", 180.0, 2),
            new Item("item3", "description3", 270.0, 3),
            new Item("item4", "description4", 360.0, 4),
            new Item("item5", "description5", 450.0, 5)
    );

    @Value("${hosts.config-service}")
    private String configServiceHost;
    @Value("${hosts.shopping-service}")
    private String shoppingServiceHost;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("config-service and shopping-service communicate correctly")
    void servicesCommunicateCorrectly() {
        log.info("Testing the communication between the services.");

        verifyShoppingItems(false);
        verifyShoppingItems(true);

        log.info("All tests passed successfully.");
    }

    private void verifyShoppingItems(boolean enableDiscount) {
        updateDiscountFeature(enableDiscount);
        List<Item> expectedItems = enableDiscount ? expectedItemsWithDiscount : expectedItemsWithoutDiscount;

        await()
                .atMost(Duration.ofSeconds(10))
                .untilAsserted(() -> verifyShoppingItems(expectedItems));
    }

    private void verifyShoppingItems(List<Item> expected) {
        ResponseEntity<List<Item>> listItemsResponse = listItems();

        assertEquals(HttpStatus.OK, listItemsResponse.getStatusCode(),
                "Failed to list items. Ensure that the shopping service is running and accessible by functional-tests on %s.".formatted(shoppingServiceHost));

        List<Item> actualItems = listItemsResponse.getBody();
        assertNotNull(actualItems, "Items listed are null.");
        expected.forEach(expectedItem ->
                assertTrue(actualItems.stream().anyMatch(expectedItem::equals),
                        "Item not found in the list. Expected items: %s, Actual items: %s".formatted(expectedItem, actualItems)));
    }

    private ResponseEntity<List<Item>> listItems() {
        RequestEntity<?> listItems = RequestEntity.get("http://%s/items".formatted(shoppingServiceHost))
                .build();
        return assertDoesNotThrow(() -> restTemplate.exchange(listItems, new ParameterizedTypeReference<>() {
                }),
                "Failed to send request to shopping-service. Make sure it is available on " + shoppingServiceHost);
    }

    private void updateDiscountFeature(boolean enableDiscount) {
        RequestEntity<?> enableDiscountRequest = RequestEntity.post(
                        "http://%s/settings/shopping-service/features.discount.available/{enabled}".formatted(configServiceHost), enableDiscount)
                .build();
        ResponseEntity<Void> enableDiscountResponse = assertDoesNotThrow(() -> restTemplate.exchange(enableDiscountRequest, Void.class),
                "Failed to send request to config-service. Make sure it is available on " + configServiceHost);
        assertEquals(HttpStatus.OK, enableDiscountResponse.getStatusCode(),
                "Failed to update the discount feature. Ensure that the config service is running and accessible by functional-tests on " + configServiceHost);
    }
}
