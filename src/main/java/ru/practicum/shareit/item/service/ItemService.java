package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item addItem(long userId, Item item);

    Item updateItem(long userId, long itemId, Item item);

    List<Item> getAllItems(long userId);

    List<Item> getItemsByText(String text);

    Item getItemById(long itemId, long userId);
}
