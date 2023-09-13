package ru.practicum.server.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.dto.CategoryDto;
import ru.practicum.server.service.CategoryService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
public class CategoryController {
    private final CategoryService service;

    @PostMapping("/admin/categories")
    @ResponseStatus(value = HttpStatus.CREATED)
    public CategoryDto createCategory(@Valid @RequestBody CategoryDto category) {
        log.debug("Контроллер - запрос на сохронение: {}", category);
        return service.create(category);
    }

    @GetMapping("/categories")
    public List<CategoryDto> getCategories(@RequestParam(defaultValue = "0") Integer from,
                                           @RequestParam(defaultValue = "10") Integer size) {
        log.debug("Контроллер - запрос на получение: from = {}, size = {}", from, size);
        return service.getAll(from, size);
    }

    @GetMapping("/categories/{categoryId}")
    public CategoryDto getCategory(@Positive @PathVariable("categoryId") Long id) {
        log.debug("Контроллер - запрос на получение: {}", id);
        return service.getById(id);
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@Positive @PathVariable("categoryId") Long id) {
        log.debug("Контроллер - запрос на удаление: {}", id);
        service.delete(id);
    }

    @PatchMapping("/admin/categories/{categoryId}")
    public CategoryDto updateCategory(@Positive @PathVariable("categoryId") Long id, @Valid @RequestBody CategoryDto categoryDto) {
        return service.update(id, categoryDto);
    }
}