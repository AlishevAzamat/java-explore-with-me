package ru.practicum.server.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.server.dto.UserDto;
import ru.practicum.server.dto.UserShortDto;
import ru.practicum.server.model.User;

@Component
public class UserMapper {
    public User toUser(UserDto userDto) {
        return User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
    }

    public UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }

    public UserShortDto toUserShortDto(User user) {
        return UserShortDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }
}