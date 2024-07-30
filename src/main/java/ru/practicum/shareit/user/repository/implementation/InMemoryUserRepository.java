package ru.practicum.shareit.user.repository.implementation;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepositoryMemory;

import java.util.*;

@Repository
public class InMemoryUserRepository implements UserRepositoryMemory {
    private final Map<Long, User> users = new HashMap<>();
    private long index = 0L;

    @Override
    public User save(User entity) {
        if (users.get(entity.getId()) == null) {
            entity.setId(++index);
        }
        users.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Optional<User> findById(long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void deleteById(long id) {
        users.remove(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return users.values()
                .stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }
}

