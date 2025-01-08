package com.nourish1709.learning.shoppingservice.api;

import com.nourish1709.learning.shoppingservice.model.dto.Item;
import com.nourish1709.learning.shoppingservice.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemsController {

    private final ItemService itemService;

    @GetMapping
    public List<Item> getItems() {
        return itemService.getItems();
    }
}
