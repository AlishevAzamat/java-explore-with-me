package ru.practicum.server.service;

import ru.practicum.server.dto.CompilationDto;
import ru.practicum.server.dto.NewCompilationDto;

import java.util.List;

public interface CompilationService {
    CompilationDto create(NewCompilationDto compilationDto);

    List<CompilationDto> getAll(Boolean pinned, int from, int size);

    CompilationDto getById(Long id);

    void delete(Long id);

    CompilationDto update(Long id, NewCompilationDto compilationDto);
}