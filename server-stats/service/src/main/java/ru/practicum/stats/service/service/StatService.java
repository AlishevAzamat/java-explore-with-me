package ru.practicum.stats.service.service;

import ru.practicum.stats.dto.HitDto;
import ru.practicum.stats.dto.StatsDto;

import java.util.List;

public interface StatService {
    HitDto create(HitDto dto);

    List<StatsDto> getStatus(String start, String end, List<String> uris, Boolean unique);
}
