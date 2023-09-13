package ru.practicum.server.service;

import ru.practicum.server.dto.RequestDto;
import ru.practicum.server.dto.UpdateRequestDtoRequest;
import ru.practicum.server.dto.UpdateRequestDtoResult;

import java.util.List;

public interface RequestService {
    RequestDto createRequest(Long eventId, Long userId);

    RequestDto cancel(Long requestId, Long userId);

    UpdateRequestDtoResult update(Long eventId, Long userId, UpdateRequestDtoRequest requestDto);

    List<RequestDto> getByUser(Long userId);

    List<RequestDto> getByEvent(Long userId, Long eventId);
}