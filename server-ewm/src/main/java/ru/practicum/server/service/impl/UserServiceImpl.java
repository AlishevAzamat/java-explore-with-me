package ru.practicum.server.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.server.dto.UserDto;
import ru.practicum.server.exception.NotFoundException;
import ru.practicum.server.mapper.UserMapper;
import ru.practicum.server.model.User;
import ru.practicum.server.repository.UserRepository;
import ru.practicum.server.service.UserService;
import ru.practicum.server.utils.PaginationUtil;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserDto create(UserDto userDto) {
        User user = userMapper.toUser(userDto);
        return userMapper.toUserDto(userRepository.save(user));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAllDtoById(List<Long> ids, int from, int size) {
        PageRequest pageRequest = PaginationUtil.getPageRequestAsc(from, size, "id");
        if (ids != null) {
            return userRepository.findByIdIn(ids, pageRequest)
                    .stream().map(userMapper::toUserDto)
                    .collect(Collectors.toList());
        } else {
            return userRepository.findAll(pageRequest)
                    .stream()
                    .map(userMapper::toUserDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Категории с id %d не найдено", id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllById(List<Long> ids) {
        if (ids != null) {
            return userRepository.findAllById(ids);
        } else {
            return List.of();
        }
    }
}