package ru.practicum.server.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ru.practicum.server.enums.SortEvent;
import ru.practicum.server.enums.State;
import ru.practicum.server.model.Category;
import ru.practicum.server.model.Event;
import ru.practicum.server.model.User;
import ru.practicum.server.repository.EventCustomRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class EventCustomRepositoryImpl implements EventCustomRepository {
    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public List<Event> findAllEventsForAdminBy(List<User> users, List<State> states, List<Category> cats, LocalDateTime start, LocalDateTime end, Pageable pageable) {
        var cb = entityManager.getCriteriaBuilder();
        var query = cb.createQuery(Event.class);
        Root<Event> eventRoot = query.from(Event.class);
        List<Predicate> predicates = new ArrayList<>();

        if (users != null) {
            predicates.add(eventRoot.get("initiator").in(users));
        }
        if (cats != null) {
            predicates.add(eventRoot.get("category").in(cats));
        }
        if (states != null) {
            predicates.add(eventRoot.get("state").in(states));
        }
        if (end != null && start != null) {
            predicates.add(cb.between(eventRoot.get("eventDate"), start, end));
        }
        query.where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(query).setMaxResults(pageable.getPageSize())
                .setFirstResult(pageable.getPageNumber()).getResultList();
    }

    //переделать
    @Override
    public List<Event> findAllEventsForUserBy(String text, Boolean paid, List<Category> cats, LocalDateTime start,
                                              LocalDateTime end, boolean onlyAvailable, SortEvent sort, Pageable pageable) {
        var cb = entityManager.getCriteriaBuilder();
        var query = cb.createQuery(Event.class);
        Root<Event> eventRoot = query.from(Event.class);
        List<Predicate> predicates = new ArrayList<>();
        if (text != null) {
            predicates.add(cb.or((cb.like(cb.upper(eventRoot.get("annotation")), ("%" + text + "%").toUpperCase())),
                    (cb.like(cb.upper(eventRoot.get("description")), ("%" + text + "%").toUpperCase()))));
        }
        if (paid != null) {
            predicates.add(cb.equal(eventRoot.get("paid"), paid));
        }
        if (cats != null) {
            predicates.add(eventRoot.get("category").in(cats));
        }
        if (start != null && end != null) {
            predicates.add(cb.between(eventRoot.get("eventDate"), start, end));
        }
        if (onlyAvailable) {
            predicates.add(cb.lt(eventRoot.get("confirmedRequests"), eventRoot.get("participantLimit")));
        }
        query.where(predicates.toArray(new Predicate[0]));
        if (sort != null) {
            switch (sort) {
                case EVENT_DATE:
                    return entityManager.createQuery(query.orderBy(cb.asc(eventRoot.get("eventDate")))).setMaxResults(pageable.getPageSize())
                            .setFirstResult(pageable.getPageNumber()).getResultList();
                case VIEWS:
                    return entityManager.createQuery(query.orderBy(cb.asc(eventRoot.get("views")))).setMaxResults(pageable.getPageSize())
                            .setFirstResult(pageable.getPageNumber()).getResultList();
            }
        }
        return entityManager.createQuery(query).setMaxResults(pageable.getPageSize())
                .setFirstResult(pageable.getPageNumber()).getResultList();
    }
}