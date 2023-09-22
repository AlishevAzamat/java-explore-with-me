package ru.practicum.server.repository;

import org.springframework.data.domain.Pageable;
import ru.practicum.server.enums.SortEvent;
import ru.practicum.server.enums.State;
import ru.practicum.server.model.Category;
import ru.practicum.server.model.Event;
import ru.practicum.server.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface EventCustomRepository {
    List<Event> findAllEventsForAdminBy(List<User> users, List<State> states, List<Category> cats, LocalDateTime start,
                                        LocalDateTime end, Pageable pageable);

    List<Event> findAllEventsForUserBy(String text, Boolean paid, List<Category> catsId, LocalDateTime start,
                                       LocalDateTime end, boolean onlyAvailable, SortEvent sort, Pageable pageable);
}