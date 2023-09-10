package ru.practicum.model;

import org.springframework.stereotype.Component;
import ru.practicum.HitDto;
import ru.practicum.StatsDto;

@Component
public class HitMapper {
    public Hit toHit(HitDto dto) {
        return Hit.builder()
                .ip(dto.getIp())
                .app(dto.getApp())
                .uri(dto.getUri())
                .timestamp(dto.getTimestamp())
                .build();
    }

    public HitDto toHitDto(Hit hit) {
        return HitDto.builder()
                .app(hit.getApp())
                .ip(hit.getIp())
                .timestamp(hit.getTimestamp())
                .uri(hit.getUri())
                .build();
    }

    public StatsDto toStatsDto(Stats hit) {
        return StatsDto.builder()
                .uri(hit.getUri())
                .app(hit.getApp())
                .hits(hit.getHits())
                .build();
    }
}
