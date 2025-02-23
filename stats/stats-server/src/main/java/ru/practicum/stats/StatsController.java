package ru.practicum.stats;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.hit.HitMapper;
import ru.practicum.hit.HitService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/stats")
@RequiredArgsConstructor
public class StatsController {

    private final HitService service;
    private final HitMapper mapper;

    @GetMapping
    public ResponseEntity<List<StatDto>> getStats(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                                  @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                                  @RequestParam(required = false) List<String> uris,
                                                  @RequestParam(required = false) boolean unique) {

        List<Stat> stats = service.getStats(start, end, uris, unique);

        return new ResponseEntity<>(mapper.toStatDto(stats), HttpStatus.OK);
    }
}
