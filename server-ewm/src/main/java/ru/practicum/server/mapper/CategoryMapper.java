package ru.practicum.server.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.server.dto.CategoryDto;
import ru.practicum.server.model.Category;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryMapper {
    public Category toCategory(CategoryDto categoryDto) {
        return Category.builder()
                .name(categoryDto.getName())
                .build();
    }

    public CategoryDto toCategoryDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public List<CategoryDto> toCategoryDto(List<Category> categories) {
        return categories.stream()
                .map(this::toCategoryDto)
                .collect(Collectors.toList());
    }
}