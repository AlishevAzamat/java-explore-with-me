package ru.practicum.server.service;

import ru.practicum.server.dto.EventDto;
import ru.practicum.server.dto.EventShortDto;
import ru.practicum.server.dto.NewEventDto;
import ru.practicum.server.dto.UpdateEventDto;
import ru.practicum.server.model.Event;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventService {
    EventDto create(NewEventDto newEventDto, Long userId);

    List<EventDto> getAll(List<Long> users, List<String> states, List<Long> catsId, String startStr, String endStr, int from, int size);

    EventDto updatePublication(Long id, UpdateEventDto eventDto);

    List<EventDto> getAllByUser(Long userId, int from, int size);

    List<EventDto> getAllPublic(String text, Boolean paid, List<Long> catsId, String startStr, String endStr,
                                boolean onlyAvailable, String sortStr, int from, int size, HttpServletRequest request);

    EventDto getPublicById(Long id, HttpServletRequest request);

    EventDto getForUserById(Long userId, Long eventId);

    EventDto update(Long userId, Long eventId, UpdateEventDto eventDto);

    Event getEventById(Long id);

    Event saveEvent(Event event);

    List<Event> getAllEvents(List<Long> ids);

    List<EventShortDto> getShortEvent(List<Event> events);
}