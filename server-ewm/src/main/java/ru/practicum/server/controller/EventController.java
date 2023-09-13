package ru.practicum.server.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.dto.EventDto;
import ru.practicum.server.dto.NewEventDto;
import ru.practicum.server.dto.UpdateEventDto;
import ru.practicum.server.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
public class EventController {
    private final EventService service;

    @PostMapping("/users/{userId}/events")
    @ResponseStatus(value = HttpStatus.CREATED)
    public EventDto createEvent(@Valid @RequestBody NewEventDto newEventDto, @Positive @PathVariable Long userId) {
        log.debug("Контроллер - запрос на сохранение: {}, от инициатора с id {}", newEventDto, userId);
        return service.create(newEventDto, userId);
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
        return service.getAll(usersId, states, catsId, startStr, endStr, from, size);
    }

    @GetMapping("/users/{userId}/events")
    public List<EventDto> getEventsByUser(@Positive @PathVariable Long userId,
                                          @RequestParam(defaultValue = "0") Integer from,
                                          @RequestParam(defaultValue = "10") Integer size) {
        log.debug("Контроллер - запрос на получение от инициатора: userId = {}, from = {}, size = {}", userId, from, size);
        return service.getAllByUser(userId, from, size);
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
        return service.getAllPublic(text, paid, catsId, startStr, endStr, onlyAvailable, sortStr, from, size, request);
    }

    @GetMapping("/events/{id}")
    public EventDto getEvent(@Positive @PathVariable Long id, HttpServletRequest request) {
        log.debug("Контроллер - запрос на публичное получение: {}", id);
        log.info("client ip: {}", request.getRemoteAddr());
        log.info("endpoint path: {}", request.getRequestURI());
        return service.getPublicById(id, request);
    }

    @GetMapping("/users/{userId}/events/{eventId}")
    public EventDto getEventByUser(@Positive @PathVariable Long userId, @Positive @PathVariable Long eventId) {
        log.debug("Контроллер - запрос на получение: eventId = {}, от инициатора userId {}", eventId, userId);
        return service.getForUserById(userId, eventId);
    }

    @PatchMapping("/admin/events/{eventId}")
    public EventDto publishedEvent(@Positive @PathVariable("eventId") Long id, @Valid @RequestBody UpdateEventDto eventDto) {
        log.debug("Контроллер - запрос на подтверждение/отклонение публикации: eventId = {}, published = {}", id, eventDto);
        return service.published(id, eventDto);
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    public EventDto updateEvent(@Positive @PathVariable Long userId, @Positive @PathVariable Long eventId,
                                @Valid @RequestBody UpdateEventDto eventDto) {
        log.debug("Контроллер - запрос отклонение публикации от пользователя: userId = {}, eventId = {}, published = {}",
                userId, eventId, eventDto);
        return service.update(userId, eventId, eventDto);
    }
}