package ru.practicum.event;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventSearchSort;
import ru.practicum.event.dto.EventShortDto;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventPublicController {

    private final EventService service;
    private final EventMapper mapper;

    @GetMapping
    public ResponseEntity<List<EventShortDto>> searchEvents(@RequestParam(required = false) String text,
                                            @RequestParam(required = false) List<Long> categories,
                                            @RequestParam(required = false) Boolean paid,
                                            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                            @RequestParam(defaultValue = "false") boolean onlyAvailable,
                                            @RequestParam(defaultValue = "EVENT_DATE") EventSearchSort sort,
                                            @RequestParam(defaultValue = "0") long from,
                                            @RequestParam(defaultValue = "10") long size) {

        // TODO: обращение к этому эндпоинту сохранить в сервисе статистики

        List<Event> events = service.searchEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
        return new ResponseEntity<>(mapper.toShortDto(events), HttpStatus.OK);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventFullDto> getEvent(@PathVariable Long eventId) {

        // TODO: обращение к этому эндпоинту сохранить в сервисе статистики

        Event event = service.getPublishedEventById(eventId);
        return new ResponseEntity<>(mapper.toFullDto(event), HttpStatus.OK);
    }
}
