package ru.practicum.shareit.item.repository.implementation;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.util.*;

@Repository
public class InMemoryItemRepository implements ItemRepository {
    private final Map<Long, Item> items = new HashMap<>();
    private long index = 0L;

    @Override
    public Item save(Item entity) {
        if (entity.getId() == null) {
            entity.setId(++index);
        }
        items.put(entity.getId(), entity);
        return items.get(entity.getId());
    }

    @Override
    public Optional<Item> findById(long id) {
        Item item = items.get(id);
        return Optional.ofNullable(item);
    }

    @Override
    public List<Item> findAll() {
        return new ArrayList<>(items.values());
    }

    @Override
    public void deleteById(long id) {
        items.remove(id);
    }

    @Override
    public List<Item> findByUserId(long userId) {
        return items.values()
                .stream()
                .filter(item -> item.getOwner().getId().equals(userId))
                .toList();
    }

    @Override
    public List<Item> findByText(String text) {
        if (text == null || text.isEmpty()) {
            return Collections.emptyList();
        } else
            return items.values()
                    .stream()
                    .filter(item ->
                            (item.getName().toLowerCase().contains(text)
                                    || item.getDescription().toLowerCase().contains(text))
                                    && (item.getAvailable()))
                    .toList();
    }
}

