package ru.practicum.server.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.server.dto.CommentDto;
import ru.practicum.server.dto.EventWithCommentDto;
import ru.practicum.server.dto.RequestCommentDto;
import ru.practicum.server.enums.SortComment;
import ru.practicum.server.enums.State;
import ru.practicum.server.exception.ConflictException;
import ru.practicum.server.exception.NotFoundException;
import ru.practicum.server.exception.ValidationException;
import ru.practicum.server.mapper.CommentMapper;
import ru.practicum.server.model.Comment;
import ru.practicum.server.model.Event;
import ru.practicum.server.repository.CommentRepository;
import ru.practicum.server.service.CommentService;
import ru.practicum.server.service.EventService;
import ru.practicum.server.service.UserService;
import ru.practicum.server.utils.PaginationUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final EventService eventService;
    private final CommentMapper commentMapper;

    @Override
    @Transactional
    public CommentDto create(Long eventId, Long userId, RequestCommentDto commentDto) {
        Event event = eventService.getEventById(eventId);
        if (event.getState() == State.PUBLISHED) {
            Comment comment = commentMapper.toComment(commentDto, event, userService.getUser(userId));
            return commentMapper.toCommentDto(commentRepository.save(comment));
        } else {
            throw new ConflictException("Комментарий можно написать только к опубликовонным событиям");
        }
    }

    @Override
    @Transactional
    public CommentDto update(Long userId, Long commentId, RequestCommentDto commentDto) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new NotFoundException(String.format("Комментария с id %d не найдено", commentId)));
        if (!comment.getAuthor().getId().equals(userId)) {
            throw new ValidationException("Вы не являетей создателем этого комментария");
        } else {
            comment.setText(commentDto.getText());
        }
        return commentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventWithCommentDto> getCommentEventDtoByUser(Long userId, int from, int size) {
        if (userId == 0) {
            throw new ValidationException("Пользователь не может быть с id равным 0");
        }
        int pageNumber = (from + size - 1) / size;
        List<Long> eventsId = commentRepository.findByAuthorId(userId)
                .stream()
                .map(comment -> comment.getEvent().getId())
                .collect(Collectors.toList());
        return eventService.getCommentEventDto(eventsId, pageNumber, size);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> getAllCommentDto(String startTimeStr, String endTimeStr, String sort, int from, int size) {
        PageRequest pageRequest;
        SortComment sorts = SortComment.fromString(sort);
        LocalDateTime start = null;
        LocalDateTime end = null;
        List<CommentDto> commentDtos;

        if (startTimeStr != null) {
            start = dateFromString(startTimeStr);
        }
        if (endTimeStr != null) {
            end = dateFromString(endTimeStr);
        }

        if (start != null && end != null) {
            if ((start.isAfter(end) || start.isEqual(end))) {
                throw new ValidationException("Start не должен быть позже end или быть равным ему.");
            }
        }

        if (sorts == SortComment.NEW) {
            pageRequest = PaginationUtil.getPageRequestDesc(from, size, "created");
        } else {
            pageRequest = PaginationUtil.getPageRequestAsc(from, size, "created");
        }

        if (start == null && end == null) {
            commentDtos = commentRepository.findAll(pageRequest).stream()
                    .map(commentMapper::toCommentDto)
                    .collect(Collectors.toList());
        } else if (start != null && end != null) {
            commentDtos = commentRepository.findByCreatedBeforeAndCreatedAfter(end, start, pageRequest)
                    .stream()
                    .map(commentMapper::toCommentDto)
                    .collect(Collectors.toList());
        } else {
            throw new ValidationException("Нужно указывать либо оба параметра start и end, либо не указывать их вообще.");
        }
        return commentDtos;
    }


    @Override
    @Transactional
    public void deleteByUser(Long userId, List<Long> commentsId) {
        if (commentsId == null) {
            if (userId != 0) {
                commentRepository.deleteByAuthorId(userId);
            } else {
                throw new ValidationException("Id пользователя не должно быть 0");
            }
        } else {
            List<Long> validComId = commentsId.stream().filter(comId -> comId <= 0).collect(Collectors.toList());
            if (validComId.size() > 0) {
                throw new ValidationException("Id комментариев не должно быть меньше или равным 0");
            }
            List<Comment> comments = commentRepository.findAllById(commentsId);
            List<Long> validComments = comments.stream()
                    .filter(comment -> !comment.getAuthor().getId().equals(userId))
                    .map(Comment::getId)
                    .collect(Collectors.toList());
            if (validComments.size() > 0) {
                throw new ValidationException(String.format("Вы не являетей создателем комментариев: %s", validComments));
            } else {
                commentRepository.deleteAllById(commentsId);
            }
        }
    }

    @Override
    @Transactional
    public void deleteByAdmin(List<Long> commentsId) {
        if (commentsId != null) {
            List<Long> validComId = commentsId.stream().filter(comId -> comId <= 0).collect(Collectors.toList());
            if (validComId.size() > 0) {
                throw new ValidationException("Id комментариев не должно быть меньше или равным 0");
            }
            try {
                commentRepository.deleteAllById(commentsId);
            } catch (EmptyResultDataAccessException e) {
                throw new ConflictException("Возможно некоторые комментарии были удалены, проверьте перед повторным удалением.");
            }
        }
    }

    private LocalDateTime dateFromString(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(dateStr, formatter);
    }
}