package ru.practicum.server.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.server.dto.CompilationDto;
import ru.practicum.server.dto.EventShortDto;
import ru.practicum.server.dto.NewCompilationDto;
import ru.practicum.server.model.Compilation;

import java.util.List;

@Component
public class CompilationMapper {
    public Compilation toCompilation(NewCompilationDto compilationDto) {
        return Compilation.builder()
                .title(compilationDto.getTitle())
                .pinned(compilationDto.getPinned())
                .build();
    }

    public CompilationDto toCompilationDto(Compilation compilation, List<EventShortDto> shortDtos) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .events(shortDtos)
                .build();
    }
}