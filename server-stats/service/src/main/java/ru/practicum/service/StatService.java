package ru.practicum.service;

import ru.practicum.HitDto;
import ru.practicum.StatsDto;

import java.util.List;

public interface StatService {
    HitDto create(HitDto dto);

    List<StatsDto> getStatus(String start, String end, List<String> uris, Boolean unique);
}
