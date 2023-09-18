package ru.practicum.stats.service.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stats.dto.HitDto;
import ru.practicum.stats.dto.NewHitDto;
import ru.practicum.stats.dto.StatsDto;
import ru.practicum.stats.service.service.StatService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StatController {
    private final StatService statService;

    @PostMapping("/hit")
    @ResponseStatus(value = HttpStatus.CREATED)
    public HitDto createStat(@RequestBody @Valid NewHitDto dto) {
        log.info("Обновление статистики: сохранение {}", dto);
        return statService.create(dto);
    }

    @GetMapping("/stats")
    public List<StatsDto> getStats(@RequestParam("start") String startStr,
                                   @RequestParam("end") String endStr,
                                   @RequestParam(required = false) List<String> uris,
                                   @RequestParam(defaultValue = "false", required = false) Boolean unique) {
        log.info("Получение статистики с параметрами: start {}, end {}, uris {}, unique {}",
                startStr, endStr, uris, unique);
        return statService.getStatus(startStr, endStr, uris, unique);
    }

    @GetMapping("/stats/views")
    public Long getStats(@RequestParam String uris) {
        log.info("Получение статистики для event: uris {}",
                uris);
        return statService.getViews(uris);
    }
}