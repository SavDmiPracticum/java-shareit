package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import java.util.List;

public interface UserService {
    List<UserDTO> getAllUsers();

    UserDTO createUser(UserDTO userDto);

    UserDTO updateUser(long id, UserUpdateDto userDto);

    UserDTO getUser(long id);

    void deleteUser(long id);
}
