package ru.practicum.shareit.user.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.common.Patcher;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User createUser(User user) {
        userRepository.findByEmail(user.getEmail()).ifPresent(
                u -> {
                    throw new ValidationException("User with email " + u.getEmail() + " already exists");
                });
        return userRepository.save(user);
    }

    @Override
    public User updateUser(long id, User user) {
        User updatedUser = userRepository.findById(id).orElseThrow(() -> new NotFoundException(
                "User with id " + user.getId() + " not found"));
        userRepository.findByEmail(user.getEmail()).ifPresent(
                u -> {
                    if (!updatedUser.getEmail().equals(u.getEmail())) {
                        throw new ValidationException("User with email " + u.getEmail() + " already exists");
                    }
                });
        try {
            Patcher.patch(updatedUser, user);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return userRepository.save(updatedUser);
    }

    @Override
    public User getUser(long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException(
                "User with id " + id + " not found"));
    }

    @Override
    public void deleteUser(long id) {
        userRepository.findById(id).orElseThrow(() -> new NotFoundException(
                "User with id " + id + " not found"));
        userRepository.deleteById(id);
    }
}
