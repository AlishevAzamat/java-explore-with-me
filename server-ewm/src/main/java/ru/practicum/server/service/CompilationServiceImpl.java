package ru.practicum.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.server.dto.CompilationDto;
import ru.practicum.server.dto.NewCompilationDto;
import ru.practicum.server.exception.NotFoundException;
import ru.practicum.server.exception.ValidationException;
import ru.practicum.server.mapper.CompilationMapper;
import ru.practicum.server.model.Compilation;
import ru.practicum.server.repository.CompilationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository repository;
    private final CompilationMapper mapper;
    private final EventService eventService;

    @Override
    @Transactional
    public CompilationDto create(NewCompilationDto newCompilationDto) {
        Compilation compilation = mapper.toCompilation(newCompilationDto);
        if (compilation.getPinned() == null) {
            compilation.setPinned(false);
        }
        compilation.setEvents(eventService.getAllEvents(newCompilationDto.getEvents()));
        compilation = repository.save(compilation);
        return mapper.toCompilationDto(compilation,
                eventService.getShortEvent(compilation.getEvents()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompilationDto> getAll(Boolean pinned, int from, int size) {
        int pageNumber = (int) Math.ceil((double) from / size);
        if (pinned == null) {
            return repository.findAll(PageRequest.of(pageNumber, size)).stream()
                    .map(compilation -> mapper.toCompilationDto(compilation,
                            eventService.getShortEvent(compilation.getEvents())))
                    .collect(Collectors.toList());
        } else {
            return repository.findByPinned(pinned, PageRequest.of(pageNumber, size)).stream()
                    .map(compilation -> mapper.toCompilationDto(compilation,
                            eventService.getShortEvent(compilation.getEvents())))
                    .collect(Collectors.toList());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public CompilationDto getById(Long id) {
        Compilation compilation = repository.findById(id).orElseThrow(() ->
                new NotFoundException("Данной подборки не существует"));
        return mapper.toCompilationDto(compilation, eventService.getShortEvent(compilation.getEvents()));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public CompilationDto update(Long id, NewCompilationDto compilationDto) {
        Compilation compilation = repository.findById(id).orElseThrow(() ->
                new NotFoundException("Данной подборки не существует"));
        if (compilationDto.getTitle() != null) {
            if (compilationDto.getTitle().isBlank() || compilationDto.getTitle().length() > 50) {
                throw new ValidationException("Название не может быть пустым или больше 50");
            } else {
                compilation.setTitle(compilationDto.getTitle());
            }
        }
        if (compilationDto.getPinned() != null) {
            compilation.setPinned(compilationDto.getPinned());
        }
        if (compilationDto.getEvents() != null) {
            compilation.setEvents(eventService.getAllEvents(compilationDto.getEvents()));
        }
        compilation = repository.save(compilation);
        return mapper.toCompilationDto(compilation, eventService.getShortEvent(compilation.getEvents()));
    }
}