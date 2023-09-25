package ru.practicum.server.service;

import ru.practicum.server.dto.CommentDto;
import ru.practicum.server.dto.EventWithCommentDto;
import ru.practicum.server.dto.RequestCommentDto;

import java.util.List;

public interface CommentService {
    CommentDto create(Long eventId, Long userId, RequestCommentDto commentDto);

    CommentDto update(Long userId, Long comId, RequestCommentDto commentDto);

    List<EventWithCommentDto> getCommentEventDtoByUser(Long userId, int from, int size);

    List<CommentDto> getAllCommentDto(String startStr, String endStr, String sort, int from, int size);

    void deleteByUser(Long userId, List<Long> comsId);

    void deleteByAdmin(List<Long> comsId);
}