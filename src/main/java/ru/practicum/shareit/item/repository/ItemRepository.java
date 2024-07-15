package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.common.CommonCrudRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends CommonCrudRepository<Item> {
    List<Item> findByUserId(long userId);

    List<Item> findByText(String text);
}
