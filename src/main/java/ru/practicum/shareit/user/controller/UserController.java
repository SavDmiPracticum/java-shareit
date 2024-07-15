package ru.practicum.shareit.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.PatchUserDto;
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
    private final UserMapper userMapper;

    @PostMapping
    public UserDTO createUser(@Valid @RequestBody UserDTO userDto) {
        User user = userService.createUser(userMapper.toUser(userDto));
        log.info("User created: {}", user);
        return userMapper.toUserDTO(user);
    }

    @PatchMapping("/{id}")
    public UserDTO updateUser(@PathVariable Long id, @Valid @RequestBody PatchUserDto userDto) {
        User user = userService.updateUser(id, userMapper.updateUser(userDto));
        log.info("User updated: {}", user);
        return userMapper.toUserDTO(user);
    }

    @GetMapping("/{id}")
    public UserDTO getUser(@PathVariable long id) {
        log.info("User get: {}", id);
        return userMapper.toUserDTO(userService.getUser(id));
    }

    @GetMapping
    public List<UserDTO> getAllUsers() {
        log.info("Get all users");
        return userMapper.toUserDTOs(userService.getAllUsers());
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable long id) {
        log.info("User deleted: {}", id);
        userService.deleteUser(id);
    }
}
