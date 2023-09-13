package ru.practicum.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.server.dto.UserDto;
import ru.practicum.server.exception.NotFoundException;
import ru.practicum.server.mapper.UserMapper;
import ru.practicum.server.model.User;
import ru.practicum.server.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper mapper;

    @Override
    @Transactional
    public UserDto create(UserDto userDto) {
        User user = mapper.toUser(userDto);
        return mapper.toUserDto(repository.save(user));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAllDtoById(List<Long> ids, int from, int size) {
        int pageNumber = (int) Math.ceil((double) from / size);
        if (ids != null) {
            return repository.findByIdIn(ids, PageRequest.of(pageNumber, size, Sort.by("id").ascending()))
                    .stream().map(mapper::toUserDto)
                    .collect(Collectors.toList());
        } else {
            return repository.findAll(PageRequest.of(pageNumber, size, Sort.by("id").ascending()))
                    .stream()
                    .map(mapper::toUserDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUser(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Категории с id %d не найдено", id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllById(List<Long> ids) {
        if (ids != null) {
            return repository.findAllById(ids);
        } else {
            return List.of();
        }
    }
}