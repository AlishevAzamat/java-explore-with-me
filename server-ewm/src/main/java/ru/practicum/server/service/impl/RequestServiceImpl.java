package ru.practicum.server.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.server.dto.RequestDto;
import ru.practicum.server.dto.UpdateRequestDtoRequest;
import ru.practicum.server.dto.UpdateRequestDtoResult;
import ru.practicum.server.enums.State;
import ru.practicum.server.enums.Status;
import ru.practicum.server.exception.ConflictException;
import ru.practicum.server.exception.NotFoundException;
import ru.practicum.server.exception.ValidationException;
import ru.practicum.server.mapper.RequestMapper;
import ru.practicum.server.model.Event;
import ru.practicum.server.model.Request;
import ru.practicum.server.model.User;
import ru.practicum.server.repository.RequestRepository;
import ru.practicum.server.service.EventService;
import ru.practicum.server.service.RequestService;
import ru.practicum.server.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserService userService;
    private final EventService eventService;
    private final RequestMapper requestMapper;

    @Override
    @Transactional
    public RequestDto createRequest(Long eventId, Long userId) {
        Event event = eventService.getEventById(eventId);
        User requester = userService.getUser(userId);

        validateRequestCreation(event, requester);

        Request request = Request.builder()
                .requester(requester)
                .event(event)
                .created(LocalDateTime.now())
                .status(Status.PENDING)
                .build();

        if (event.getParticipantLimit() == 0 || !event.getRequestModeration()) {
            request.setStatus(Status.CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventService.saveEvent(event);
        }

        return requestMapper.toRequestDto(requestRepository.save(request));
    }

    @Override
    @Transactional
    public RequestDto cancel(Long requestId, Long userId) {
        User user = userService.getUser(userId);
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(String.format("Категории с id %d не найдено", requestId)));
        if (!user.getId().equals(request.getRequester().getId())) {
            throw new ValidationException("Вы не запрашивали участие на это событие.");
        }
        request.setStatus(Status.CANCELED);
        return requestMapper.toRequestDto(requestRepository.save(request));
    }

    @Override
    @Transactional
    public UpdateRequestDtoResult update(Long eventId, Long userId, UpdateRequestDtoRequest requestDto) {
        User user = userService.getUser(userId);
        Event event = eventService.getEventById(eventId);
        if (!event.getInitiator().getId().equals(user.getId())) {
            throw new ConflictException("Вы не являетесь инициатором события, не возможно изменить статус заявок.");
        }
        List<Request> requests = requestRepository.findAllById(requestDto.getRequestIds());
        List<Request> filterRequest = requests.stream().filter(request -> request.getStatus() == Status.CONFIRMED)
                .collect(Collectors.toList());
        if (filterRequest.size() == 0) {
            requests = requests.stream().peek(request -> request.setStatus(requestDto.getStatus()))
                    .collect(Collectors.toList());
        } else {
            throw new ConflictException("Невозможно изменить так как уже принято или отклонённая заявка.");
        }
        List<RequestDto> requestDtos = requestRepository.saveAll(requests).stream()
                .map(requestMapper::toRequestDto).collect(Collectors.toList());
        switch (requestDto.getStatus()) {
            case REJECTED:
                return UpdateRequestDtoResult.builder().rejectedRequests(requestDtos).build();
            case CONFIRMED:
                if (event.getParticipantLimit().equals(event.getConfirmedRequests())) {
                    throw new ConflictException("Вы не можете принять данную заявку, так как лимит будет превышен");
                }
                event.setConfirmedRequests(event.getConfirmedRequests() + requestDtos.size());
                eventService.saveEvent(event);
                return UpdateRequestDtoResult.builder().confirmedRequests(requestDtos).build();
            default:
                throw new ValidationException("Вы можете только подтвердить или отказать заявкам на участие.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequestDto> getByUser(Long userId) {
        User user = userService.getUser(userId);
        return requestRepository.findByRequesterId(user.getId()).stream().map(requestMapper::toRequestDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequestDto> getByEvent(Long userId, Long eventId) {
        User user = userService.getUser(userId);
        Event event = eventService.getEventById(eventId);
        if (!event.getInitiator().getId().equals(user.getId())) {
            throw new ConflictException("Вы не являетесь инициатором события, не возможно получить список заявок.");
        }
        return requestRepository.findByEventId(event.getId()).stream().map(requestMapper::toRequestDto).collect(Collectors.toList());
    }

    private void validateRequestCreation(Event event, User requester) {
        if (requester.getId().equals(event.getInitiator().getId())) {
            throw new ConflictException("Вы являетесь инициатором события.");
        }
        if (event.getState() != State.PUBLISHED) {
            throw new ConflictException("Вы не можете запросить участие в неопубликованном событии.");
        }
        if (requestRepository.findByRequesterIdAndEventId(requester.getId(), event.getId()).isPresent()) {
            throw new ConflictException("Вы уже подавали заявку на участие в этом событии.");
        }
        if (event.getParticipantLimit() <= event.getConfirmedRequests() && event.getParticipantLimit() != 0) {
            throw new ConflictException("Запросы на данное событие уже превышают лимит.");
        }
    }
}