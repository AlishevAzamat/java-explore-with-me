package ru.practicum.server.service;

import ru.practicum.server.dto.CategoryDto;
import ru.practicum.server.model.Category;

import java.util.List;

public interface CategoryService {
    CategoryDto create(CategoryDto categoryDto);

    List<CategoryDto> getAll(int from, int size);

    CategoryDto getById(Long id);

    void delete(Long id);

    CategoryDto update(Long id, CategoryDto categoryDto);

    Category getCategory(Long id);

    List<Category> getAllById(List<Long> ids);
}