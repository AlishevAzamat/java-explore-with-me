package ru.practicum;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface StatRepository extends JpaRepository<Hit, Long> {
    @Query("select new ru.practicum.model.Stats(app, uri, count(uri) as hits) from Hit " +
            "where uri in (?1) " +
            "and timestamp >= ?2 and timestamp <= ?3 " +
            "group by app, uri order by hits desc")
    List<Stats> findStatsWithoutUnique(List<String> uris, LocalDateTime start, LocalDateTime end);

    @Query("select new ru.practicum.model.Stats(app, uri, count(distinct uri) as hits) from Hit " +
            "where uri in (?1) " +
            "and timestamp >= ?2 and timestamp <= ?3 " +
            "group by app, uri order by hits")
    List<Stats> findStats(List<String> uris, LocalDateTime start, LocalDateTime end);

    @Query("select new ru.practicum.model.Stats(app, uri, count(uri) as hits) from Hit " +
            "where timestamp >= ?1 and timestamp <= ?2 " +
            "group by app, uri order by hits")
    List<Stats> findStatsWithoutUrisAndUnique(LocalDateTime start, LocalDateTime end);

    @Query("select new ru.practicum.model.Stats(app, uri, count(distinct uri) as hits) from Hit " +
            "where timestamp >= ?1 and timestamp <= ?2 " +
            "group by app, uri order by hits desc")
    List<Stats> findStatsWithoutUris(LocalDateTime start, LocalDateTime end);
}