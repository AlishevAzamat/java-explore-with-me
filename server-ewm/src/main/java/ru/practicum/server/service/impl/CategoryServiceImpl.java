package ru.practicum.server.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.server.dto.CategoryDto;
import ru.practicum.server.exception.NotFoundException;
import ru.practicum.server.mapper.CategoryMapper;
import ru.practicum.server.model.Category;
import ru.practicum.server.repository.CategoryRepository;
import ru.practicum.server.service.CategoryService;
import ru.practicum.server.utils.PaginationUtil;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public CategoryDto create(CategoryDto categoryDto) {
        Category category = categoryMapper.toCategory(categoryDto);
        return categoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> getAll(int from, int size) {
        PageRequest pageRequest = PaginationUtil.getPageRequestAsc(from, size, "id");
        List<Category> categories = categoryRepository.findAll(pageRequest).getContent();
        return categoryMapper.toCategoryDto(categories);
    }

    @Override
    public CategoryDto getById(Long id) {
        return categoryMapper.toCategoryDto(getCategory(id));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    @Transactional
    public CategoryDto update(Long id, CategoryDto categoryDto) {
        Category category = getCategory(id);
        category.setName(categoryDto.getName());
        return categoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    @Override
    @Transactional(readOnly = true)
    public Category getCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Категории с id %d не найдено", id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> getAllById(List<Long> ids) {
        return categoryRepository.findAllById(ids);
    }
}