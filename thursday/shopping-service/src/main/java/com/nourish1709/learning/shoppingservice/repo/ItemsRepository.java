package com.nourish1709.learning.shoppingservice.repo;

import com.nourish1709.learning.shoppingservice.model.dao.ItemEntity;
import com.nourish1709.learning.shoppingservice.model.dto.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemsRepository extends JpaRepository<ItemEntity, Long> {

    @Query("select new com.nourish1709.learning.shoppingservice.model.dto.Item(i.name, i.description, i.price, i.quantity) from ItemEntity i")
    List<Item> findAllItems();
}
