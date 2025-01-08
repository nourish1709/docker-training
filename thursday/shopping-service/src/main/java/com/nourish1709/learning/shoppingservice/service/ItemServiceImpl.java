package com.nourish1709.learning.shoppingservice.service;

import com.nourish1709.learning.shoppingservice.model.dto.Item;
import com.nourish1709.learning.shoppingservice.repo.ItemsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemsRepository repository;
    private final DiscountService discountService;

    @Override
    public List<Item> getItems() {
        if (discountService.isDiscountAvailable()) {
            return repository.findAllItems().stream()
                    .map(item -> new Item(item.name(), item.description(), item.price() * 0.9, item.quantity()))
                    .toList();
        }

        return repository.findAllItems();
    }
}
