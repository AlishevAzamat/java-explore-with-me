package ru.practicum.stats.service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.stats.dto.HitDto;
import ru.practicum.stats.dto.NewHitDto;
import ru.practicum.stats.dto.StatsDto;
import ru.practicum.stats.service.repository.StatRepository;
import ru.practicum.stats.service.exception.ValidationException;
import ru.practicum.stats.service.model.Hit;
import ru.practicum.stats.service.model.HitMapper;
import ru.practicum.stats.service.model.Stats;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatServiceImpl implements StatService {
    private final StatRepository statRepository;
    private final HitMapper hitMapper;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public HitDto create(NewHitDto dto) {
        Hit hit = statRepository.save(hitMapper.toHit(dto));
        return hitMapper.toHitDto(hit);
    }

    @Override
    public List<StatsDto> getStatus(String startStr, String endStr, List<String> uris, Boolean unique) {
        List<Stats> hits;
        LocalDateTime start = LocalDateTime.parse(startStr, formatter);
        LocalDateTime end = LocalDateTime.parse(endStr, formatter);
        if (start.isEqual(end) || start.isAfter(end)) {
            throw new ValidationException("Начало не должно быть позже конца и время не должно совпадать.");
        }
        if (uris != null) {
            if (unique) {
                hits = statRepository.findStats(uris, start, end);
            } else {
                hits = statRepository.findStatsWithoutUnique(uris, start, end);
            }
        } else {
            if (unique) {
                hits = statRepository.findStatsWithoutUris(start, end);
            } else {
                hits = statRepository.findStatsWithoutUrisAndUnique(start, end);
            }
        }
        return hits.stream().map(hitMapper::toStatsDto).collect(Collectors.toList());
    }

    @Override
    public Long getViews(String uris) {
        return statRepository.findStatsUrisAndUnique(uris).getHits();
    }
}