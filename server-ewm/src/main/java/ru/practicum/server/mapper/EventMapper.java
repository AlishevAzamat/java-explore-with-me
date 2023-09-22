package ru.practicum.server.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.server.dto.*;
import ru.practicum.server.enums.State;
import ru.practicum.server.model.Event;
import ru.practicum.server.model.Location;

import java.time.LocalDateTime;

@Component
public class EventMapper {
    public Event toEvent(NewEventDto eventDto) {
        return Event.builder()
                .annotation(eventDto.getAnnotation())
                .description(eventDto.getDescription())
                .eventDate(eventDto.getEventDate())
                .lat(eventDto.getLocation().getLat())
                .lon(eventDto.getLocation().getLon())
                .paid(eventDto.getPaid())
                .participantLimit(eventDto.getParticipantLimit())
                .requestModeration(eventDto.getRequestModeration())
                .title(eventDto.getTitle())
                .views(0L)
                .createdOn(LocalDateTime.now())
                .state(State.PENDING)
                .confirmedRequests(0)
                .build();
    }

    public EventDto toEventDto(Event event, UserShortDto userShortDto, CategoryDto categoryDto) {
        return EventDto.builder()
                .annotation(event.getAnnotation())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .requestModeration(event.getRequestModeration())
                .title(event.getTitle())
                .id(event.getId())
                .state(event.getState())
                .createdOn(event.getCreatedOn())
                .publishedOn(event.getPublishedOn())
                .location(Location.builder().lat(event.getLat()).lon(event.getLon()).build())
                .confirmedRequests(event.getConfirmedRequests())
                .initiator(userShortDto)
                .category(categoryDto)
                .views(event.getViews())
                .build();
    }

    public EventShortDto toEventShortDto(Event event, UserShortDto userShortDto, CategoryDto categoryDto) {
        return EventShortDto.builder()
                .annotation(event.getAnnotation())
                .eventDate(event.getEventDate())
                .paid(event.getPaid())
                .title(event.getTitle())
                .category(categoryDto)
                .initiator(userShortDto)
                .id(event.getId())
                .views(event.getViews())
                .build();
    }

    public CommentEventDto toCommentEventDto(Event event, UserShortDto userShortDto, CategoryDto categoryDto) {
        return CommentEventDto.builder()
                .annotation(event.getAnnotation())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .requestModeration(event.getRequestModeration())
                .title(event.getTitle())
                .id(event.getId())
                .state(event.getState())
                .createdOn(event.getCreatedOn())
                .publishedOn(event.getPublishedOn())
                .location(Location.builder().lat(event.getLat()).lon(event.getLon()).build())
                .confirmedRequests(event.getConfirmedRequests())
                .initiator(userShortDto)
                .category(categoryDto)
                .views(event.getViews())
                .build();
    }
}