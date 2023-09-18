package ru.practicum.server.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.server.enums.State;
import ru.practicum.server.model.Event;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends EventCustomRepository, JpaRepository<Event, Long> {
    Page<Event> findByInitiatorId(Long userId, Pageable pageable);

    Optional<Event> findByIdAndStateIn(Long id, List<State> state);
}