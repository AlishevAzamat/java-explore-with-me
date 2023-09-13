package ru.practicum.server.service;

import ru.practicum.server.dto.UserDto;
import ru.practicum.server.model.User;

import java.util.List;

public interface UserService {
    UserDto create(UserDto userDto);

    List<UserDto> getAllDtoById(List<Long> ids, int from, int size);

    void delete(Long id);

    User getUser(Long id);

    List<User> getAllById(List<Long> ids);


}