package ru.practicum.shareit.user.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.common.Patcher;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<UserDTO> getAllUsers() {
        return UserMapper.toUserDTOs(userRepository.findAll());
    }

    @Override
    @Transactional
    public UserDTO createUser(UserDTO userDto) {
        return UserMapper.toUserDTO(userRepository.save(UserMapper.toUser(userDto)));
    }

    @Override
    @Transactional
    public UserDTO updateUser(long id, UserUpdateDto userDto) {
        User user = UserMapper.updateUser(userDto);
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
        return UserMapper.toUserDTO(userRepository.save(updatedUser));
    }

    @Override
    public UserDTO getUser(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException(
                "User with id " + id + " not found"));
        return UserMapper.toUserDTO(user);
    }

    @Override
    @Transactional
    public void deleteUser(long id) {
        userRepository.findById(id).orElseThrow(() -> new NotFoundException(
                "User with id " + id + " not found"));
        userRepository.deleteById(id);
    }
}
