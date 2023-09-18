package ru.practicum.server.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.dto.*;
import ru.practicum.server.service.EventService;
import ru.practicum.server.service.RequestService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
public class PrivateController {
    private final EventService eventService;
    private final RequestService requestService;

    @PostMapping("/users/{userId}/events")
    @ResponseStatus(value = HttpStatus.CREATED)
    public EventDto createEvent(@Valid @RequestBody NewEventDto newEventDto, @Positive @PathVariable Long userId) {
        log.debug("Контроллер - запрос на сохранение: {}, от инициатора с id {}", newEventDto, userId);
        return eventService.create(newEventDto, userId);
    }

    @GetMapping("/users/{userId}/events")
    public List<EventDto> getEventsByUser(@PathVariable Long userId,
                                          @RequestParam(defaultValue = "0") Integer from,
                                          @RequestParam(defaultValue = "10") Integer size) {
        log.debug("Контроллер - запрос на получение от инициатора: userId = {}, from = {}, size = {}", userId, from, size);
        return eventService.getAllByUser(userId, from, size);
    }

    @GetMapping("/users/{userId}/events/{eventId}")
    public EventDto getEventByUser(@PathVariable Long userId, @Positive @PathVariable Long eventId) {
        log.debug("Контроллер - запрос на получение: eventId = {}, от инициатора userId {}", eventId, userId);
        return eventService.getForUserById(userId, eventId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    public EventDto updateEvent(@PathVariable Long userId, @Positive @PathVariable Long eventId,
                                @Valid @RequestBody UpdateEventDto eventDto) {
        log.debug("Контроллер - запрос отклонение публикации от пользователя: userId = {}, eventId = {}, published = {}",
                userId, eventId, eventDto);
        return eventService.update(userId, eventId, eventDto);
    }

    @GetMapping("/users/{userId}/events/{eventId}/requests")
    public List<RequestDto> getRequestByEvent(@PathVariable Long userId, @Positive @PathVariable Long eventId) {
        log.debug("Контроллер - запрос на получение: userId = {}, eventId = {}", userId, eventId);
        return requestService.getByEvent(userId, eventId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests")
    public UpdateRequestDtoResult updateRequest(@PathVariable Long eventId, @Positive @PathVariable Long userId,
                                                @Valid @RequestBody UpdateRequestDtoRequest requestDto) {
        log.debug("Контроллер - запрос на подтверждение/отклонение: eventId = {}, userId = {}, updateDto = {}", eventId, userId, requestDto);
        return requestService.update(eventId, userId, requestDto);
    }

    @PostMapping("/users/{userId}/requests")
    @ResponseStatus(value = HttpStatus.CREATED)
    public RequestDto createRequest(@RequestParam Long eventId, @Positive @PathVariable Long userId) {
        log.debug("Контроллер - запрос на сохранение: eventId = {}, userId = {}", eventId, userId);
        return requestService.createRequest(eventId, userId);
    }

    @PatchMapping("/users/{userId}/requests/{requestId}/cancel")
    public RequestDto cancelRequest(@PathVariable Long requestId, @Positive @PathVariable Long userId) {
        log.debug("Контроллер - запрос на отмену: requestId = {}, userId = {}", requestId, userId);
        return requestService.cancel(requestId, userId);
    }

    @GetMapping("/users/{userId}/requests")
    public List<RequestDto> getRequestByUser(@PathVariable Long userId) {
        log.debug("Контроллер - запрос на получение заявок от пользователя с id {}", userId);
        return requestService.getByUser(userId);
    }
}
