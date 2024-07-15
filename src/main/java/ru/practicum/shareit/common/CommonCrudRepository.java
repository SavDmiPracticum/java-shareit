package ru.practicum.shareit.common;

import java.util.List;
import java.util.Optional;

public interface CommonCrudRepository<T> {
    T save(T entity);

    Optional<T> findById(long id);

    List<T> findAll();

    void deleteById(long id);
}
