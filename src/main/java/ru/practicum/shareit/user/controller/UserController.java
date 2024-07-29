package ru.practicum.shareit.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserDTO createUser(@Valid @RequestBody UserDTO userDto) {
        log.info("User created: {}", userDto);
        return userService.createUser(userDto);
    }

    @PatchMapping("/{id}")
    public UserDTO updateUser(@PathVariable Long id, @Valid @RequestBody UserUpdateDto userDto) {
        log.info("User updated: {}", userDto);
        return userService.updateUser(id, userDto);
    }

    @GetMapping("/{id}")
    public UserDTO getUser(@PathVariable long id) {
        log.info("User get: {}", id);
        return userService.getUser(id);
    }

    @GetMapping
    public List<UserDTO> getAllUsers() {
        log.info("Get all users");
        return userService.getAllUsers();
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable long id) {
        log.info("User deleted: {}", id);
        userService.deleteUser(id);
    }
}
