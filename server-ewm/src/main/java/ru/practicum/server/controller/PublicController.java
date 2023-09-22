package ru.practicum.server.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.server.dto.CategoryDto;
import ru.practicum.server.dto.CommentEventDto;
import ru.practicum.server.dto.CompilationDto;
import ru.practicum.server.dto.EventDto;
import ru.practicum.server.service.CategoryService;
import ru.practicum.server.service.CompilationService;
import ru.practicum.server.service.EventService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
public class PublicController {
    private final CompilationService compilationService;
    private final CategoryService categoryService;
    private final EventService eventService;

    @GetMapping("/compilations")
    public List<CompilationDto> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                @RequestParam(defaultValue = "0") Integer from,
                                                @RequestParam(defaultValue = "10") Integer size) {
        log.debug("Контроллер - запрос на получение: pinned = {}, from = {}, size = {}", pinned, from, size);
        return compilationService.getAll(pinned, from, size);
    }

    @GetMapping("compilations/{compId}")
    public CompilationDto getCompilation(@PathVariable("compId") Long id) {
        log.debug("Контроллер - запрос на получение: {}", id);
        return compilationService.getById(id);
    }

    @GetMapping("/categories")
    public List<CategoryDto> getCategories(@RequestParam(defaultValue = "0") Integer from,
                                           @RequestParam(defaultValue = "10") Integer size) {
        log.debug("Контроллер - запрос на получение: from = {}, size = {}", from, size);
        return categoryService.getAll(from, size);
    }

    @GetMapping("/categories/{categoryId}")
    public CategoryDto getCategory(@PathVariable("categoryId") Long id) {
        log.debug("Контроллер - запрос на получение: {}", id);
        return categoryService.getById(id);
    }

    @GetMapping("/events")
    public List<EventDto> getEvents(@RequestParam(required = false) String text,
                                    @RequestParam(required = false) Boolean paid,
                                    @RequestParam(name = "categories", required = false) List<Long> catsId,
                                    @RequestParam(name = "rangeStart", required = false) String startStr,
                                    @RequestParam(name = "rangeEnd", required = false) String endStr,
                                    @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                    @RequestParam(name = "sort", required = false) String sortStr,
                                    @RequestParam(defaultValue = "0") Integer from,
                                    @RequestParam(defaultValue = "10") Integer size,
                                    HttpServletRequest request) {
        log.debug("Контроллер - запрос на публичное получение: text = {}, paid = {}, categories = {}, rangeStart = {}, " +
                        "rangeEnd = {}, onlyAvailable = {}, sort = {}, from = {}, size = {}", text, paid, catsId, startStr,
                endStr, onlyAvailable, sortStr, from, size);
        log.info("client ip: {}", request.getRemoteAddr());
        log.info("endpoint path: {}", request.getRequestURI());
        return eventService.getAllPublic(text, paid, catsId, startStr, endStr, onlyAvailable, sortStr, from, size, request);
    }

    @GetMapping("/events/{id}")
    public CommentEventDto getEvent(@PathVariable Long id, HttpServletRequest request) {
        log.debug("Контроллер - запрос на публичное получение: {}", id);
        log.info("client ip: {}", request.getRemoteAddr());
        log.info("endpoint path: {}", request.getRequestURI());
        return eventService.getPublicById(id, request);
    }
}
