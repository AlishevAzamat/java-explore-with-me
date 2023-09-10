package ru.practicum;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.service.StatService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StatController {
    private final StatService service;

    @PostMapping("/hit")
    public HitDto createStat(@RequestBody @Valid HitDto dto) {
        log.info("Обновление статистики: сохранение {}", dto);
        return service.create(dto);
    }

    @GetMapping("/stats")
    public List<StatsDto> getStats(@RequestParam("start") String startStr,
                                   @RequestParam("end") String endStr,
                                   @RequestParam(required = false) List<String> uris,
                                   @RequestParam(defaultValue = "false", required = false) Boolean unique) {
        log.info("Получение статистики с параметрами: start {}, end {}, uris {}, unique {}",
                startStr, endStr, uris, unique);
        return service.getStatus(startStr, endStr, uris, unique);
    }
}