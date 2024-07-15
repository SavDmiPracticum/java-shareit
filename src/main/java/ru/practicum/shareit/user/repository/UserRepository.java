package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.common.CommonCrudRepository;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

public interface UserRepository extends CommonCrudRepository<User> {
    Optional<User> findByEmail(String email);
}
