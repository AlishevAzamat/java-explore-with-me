package ru.practicum.server.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.dto.CompilationDto;
import ru.practicum.server.dto.NewCompilationDto;
import ru.practicum.server.service.CompilationService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
public class CompilationController {
    private final CompilationService service;

    @PostMapping("/admin/compilations")
    @ResponseStatus(value = HttpStatus.CREATED)
    public CompilationDto createCompilation(@Valid @RequestBody NewCompilationDto compilation) {
        log.debug("Контроллер - запрос на сохронение: {}", compilation);
        return service.create(compilation);
    }

    @GetMapping("/compilations")
    public List<CompilationDto> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                @RequestParam(defaultValue = "0") Integer from,
                                                @RequestParam(defaultValue = "10") Integer size) {
        log.debug("Контроллер - запрос на получение: pinned = {}, from = {}, size = {}", pinned, from, size);
        return service.getAll(pinned, from, size);
    }

    @GetMapping("compilations/{compId}")
    public CompilationDto getCompilation(@Positive @PathVariable("compId") Long id) {
        log.debug("Контроллер - запрос на получение: {}", id);
        return service.getById(id);
    }

    @DeleteMapping("/admin/compilations/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@Positive @PathVariable("compId") Long id) {
        log.debug("Контроллер - запрос на удаление: {}", id);
        service.delete(id);
    }

    @PatchMapping("/admin/compilations/{compId}")
    public CompilationDto updateCompilation(@Positive @PathVariable Long compId, @RequestBody NewCompilationDto compilationDto) {
        return service.update(compId, compilationDto);
    }
}