package ru.practicum.shareit.item.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.common.Patcher;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public Item addItem(long userId, Item item) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User " + userId + " not found"));
        item.setOwner(user);
        return itemRepository.save(item);
    }

    @Override
    public Item updateItem(long userId, long itemId, Item item) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User " + userId + " not found"));
        Item updatedItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item " + itemId + " not found"));
        if (!updatedItem.getOwner().getId().equals(userId)) {
            throw new NotFoundException("Item " + itemId + " is not owner of user " + userId);
        }
        try {
            Patcher.patch(updatedItem, item);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return itemRepository.save(updatedItem);
    }

    @Override
    public List<Item> getAllItems(long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User " + userId + " not found"));
        return itemRepository.findByUserId(userId);
    }

    @Override
    public List<Item> getItemsByText(String text) {
        return itemRepository.findByText(text.toLowerCase());
    }

    @Override
    public Item getItemById(long itemId, long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User " + userId + " not found"));
        return itemRepository.findById(itemId).orElseThrow(
                () -> new NotFoundException("Item " + itemId + " not found"));
    }
}
