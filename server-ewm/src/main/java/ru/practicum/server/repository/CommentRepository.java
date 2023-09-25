package ru.practicum.server.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.server.model.Comment;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByAuthorId(Long userId);

    void deleteByAuthorId(Long userId);

    List<Comment> findByEventId(Long eventId);

    Page<Comment> findByCreatedBeforeAndCreatedAfter(LocalDateTime start, LocalDateTime end, Pageable pageable);

    List<Comment> findByEventIdIn(List<Long> ids);
}