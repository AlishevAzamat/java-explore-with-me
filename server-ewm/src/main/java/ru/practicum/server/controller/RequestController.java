package ru.practicum.server.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.dto.RequestDto;
import ru.practicum.server.dto.UpdateRequestDtoRequest;
import ru.practicum.server.dto.UpdateRequestDtoResult;
import ru.practicum.server.service.RequestService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
public class RequestController {
    private final RequestService service;

    @PostMapping("/users/{userId}/requests")
    @ResponseStatus(value = HttpStatus.CREATED)
    public RequestDto createRequest(@Positive @RequestParam Long eventId, @Positive @PathVariable Long userId) {
        log.debug("Контроллер - запрос на сохранение: eventId = {}, userId = {}", eventId, userId);
        return service.createRequest(eventId, userId);
    }

    @PatchMapping("/users/{userId}/requests/{requestId}/cancel")
    public RequestDto cancelRequest(@Positive @PathVariable Long requestId, @Positive @PathVariable Long userId) {
        log.debug("Контроллер - запрос на отмену: requestId = {}, userId = {}", requestId, userId);
        return service.cancel(requestId, userId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests")
    public UpdateRequestDtoResult updateRequest(@Positive @PathVariable Long eventId, @Positive @PathVariable Long userId,
                                                @Valid @RequestBody UpdateRequestDtoRequest requestDto) {
        log.debug("Контроллер - запрос на подтверждение/отклонение: eventId = {}, userId = {}, updateDto = {}", eventId, userId, requestDto);
        return service.update(eventId, userId, requestDto);
    }

    @GetMapping("/users/{userId}/requests")
    public List<RequestDto> getRequestByUser(@Positive @PathVariable Long userId) {
        log.debug("Контроллер - запрос на получение заявок от пользователя с id {}", userId);
        return service.getByUser(userId);
    }

    @GetMapping("/users/{userId}/events/{eventId}/requests")
    public List<RequestDto> getRequestByEvent(@Positive @PathVariable Long userId, @Positive @PathVariable Long eventId) {
        log.debug("Контроллер - запрос на получение: userId = {}, eventId = {}", userId, eventId);
        return service.getByEvent(userId, eventId);
    }
}