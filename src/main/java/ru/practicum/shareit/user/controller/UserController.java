package ru.practicum.shareit.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
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
        User user = userService.createUser(UserMapper.toUser(userDto));
        log.info("User created: {}", user);
        return UserMapper.toUserDTO(user);
    }

    @PatchMapping("/{id}")
    public UserDTO updateUser(@PathVariable Long id, @Valid @RequestBody UserUpdateDto userDto) {
        User user = userService.updateUser(id, UserMapper.updateUser(userDto));
        log.info("User updated: {}", user);
        return UserMapper.toUserDTO(user);
    }

    @GetMapping("/{id}")
    public UserDTO getUser(@PathVariable long id) {
        log.info("User get: {}", id);
        return UserMapper.toUserDTO(userService.getUser(id));
    }

    @GetMapping
    public List<UserDTO> getAllUsers() {
        log.info("Get all users");
        return UserMapper.toUserDTOs(userService.getAllUsers());
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable long id) {
        log.info("User deleted: {}", id);
        userService.deleteUser(id);
    }
}
