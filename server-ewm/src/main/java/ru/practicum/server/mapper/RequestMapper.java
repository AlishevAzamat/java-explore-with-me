package ru.practicum.server.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.server.dto.RequestDto;
import ru.practicum.server.model.Request;

@Component
public class RequestMapper {
    public RequestDto toRequestDto(Request request) {
        return RequestDto.builder()
                .requester(request.getRequester().getId())
                .created(request.getCreated())
                .id(request.getId())
                .event(request.getEvent().getId())
                .status(request.getStatus())
                .build();
    }
}