package ru.practicum.server.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.dto.*;
import ru.practicum.server.service.CategoryService;
import ru.practicum.server.service.CompilationService;
import ru.practicum.server.service.EventService;
import ru.practicum.server.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
public class AdminController {
    private final CategoryService categoryService;
    private final EventService eventService;
    private final UserService userService;
    private final CompilationService compilationService;

    @PostMapping("/admin/categories")
    @ResponseStatus(value = HttpStatus.CREATED)
    public CategoryDto createCategory(@Valid @RequestBody CategoryDto category) {
        log.debug("Контроллер - запрос на сохронение: {}", category);
        return categoryService.create(category);
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable("categoryId") Long id) {
        log.debug("Контроллер - запрос на удаление: {}", id);
        categoryService.delete(id);
    }

    @PatchMapping("/admin/categories/{categoryId}")
    public CategoryDto updateCategory(@PathVariable("categoryId") Long id, @Valid @RequestBody CategoryDto categoryDto) {
        return categoryService.update(id, categoryDto);
    }

    @GetMapping("/admin/events")
    public List<EventDto> getEventsForAdmin(@RequestParam(name = "users", required = false) List<Long> usersId,
                                            @RequestParam(required = false) List<String> states,
                                            @RequestParam(name = "categories", required = false) List<Long> catsId,
                                            @RequestParam(name = "rangeStart", required = false) String startStr,
                                            @RequestParam(name = "rangeEnd", required = false) String endStr,
                                            @RequestParam(defaultValue = "0") Integer from,
                                            @RequestParam(defaultValue = "10") Integer size) {
        log.debug("Контроллер - запрос на получение администрации: users = {}, states = {}, categories = {}, " +
                "rangeStart = {}, rangeEnd = {}, from = {}, size = {}", usersId, states, catsId, startStr, endStr, from, size);
        return eventService.getAll(usersId, states, catsId, startStr, endStr, from, size);
    }

    @PatchMapping("/admin/events/{eventId}")
    public EventDto publishedEvent(@PathVariable("eventId") Long id, @Valid @RequestBody UpdateEventDto eventDto) {
        log.debug("Контроллер - запрос на подтверждение/отклонение публикации: eventId = {}, published = {}", id, eventDto);
        return eventService.updatePublication(id, eventDto);
    }

    @PostMapping("/admin/users")
    @ResponseStatus(value = HttpStatus.CREATED)
    public UserDto createUser(@Valid @RequestBody UserDto user) {
        log.debug("Контроллер - запрос на сохронение: {}", user);
        return userService.create(user);
    }

    @GetMapping("/admin/users")
    public List<UserDto> getUsers(@RequestParam(required = false) List<Long> ids,
                                  @RequestParam(defaultValue = "0") Integer from,
                                  @RequestParam(defaultValue = "10") Integer size) {
        log.debug("Контроллер - запрос на получение: {}, где from = {}, size = {}", ids, from, size);
        return userService.getAllDtoById(ids, from, size);
    }

    @DeleteMapping("/admin/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable("userId") Long id) {
        log.debug("Контроллер - запрос на удаление: {}", id);
        userService.delete(id);
    }

    @PostMapping("/admin/compilations")
    @ResponseStatus(value = HttpStatus.CREATED)
    public CompilationDto createCompilation(@Valid @RequestBody NewCompilationDto compilation) {
        log.debug("Контроллер - запрос на сохронение: {}", compilation);
        return compilationService.create(compilation);
    }

    @DeleteMapping("/admin/compilations/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable("compId") Long id) {
        log.debug("Контроллер - запрос на удаление: {}", id);
        compilationService.delete(id);
    }

    @PatchMapping("/admin/compilations/{compId}")
    public CompilationDto updateCompilation(@PathVariable Long compId, @RequestBody NewCompilationDto compilationDto) {
        return compilationService.update(compId, compilationDto);
    }
}
