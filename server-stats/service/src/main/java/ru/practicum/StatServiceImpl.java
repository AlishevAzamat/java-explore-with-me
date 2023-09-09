package ru.practicum;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatServiceImpl implements StatService {
    private final StatRepository repository;
    private final HitMapper mapper;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public HitDto create(HitDto dto) {
        Hit hit = repository.save(mapper.toHit(dto));
        return mapper.toHitDto(hit);
    }

    @Override
    public List<StatsDto> getStatus(String startStr, String endStr, List<String> uris, Boolean unique) {
        LocalDateTime start = LocalDateTime.parse(startStr, formatter);
        LocalDateTime end = LocalDateTime.parse(endStr, formatter);
        List<Stats> hits;
        if (uris != null) {
            if (unique) {
                hits = repository.findStats(uris, start, end);
            } else {
                hits = repository.findStatsWithoutUnique(uris, start, end);
            }
        } else {
            if (unique) {
                hits = repository.findStatsWithoutUris(start, end);
            } else {
                hits = repository.findStatsWithoutUrisAndUnique(start, end);
            }
        }
        return hits.stream().map(mapper::toStatsDto).collect(Collectors.toList());
    }
}