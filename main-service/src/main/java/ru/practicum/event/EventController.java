package ru.practicum.event;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/events")
public class EventController {

    private final EventService service;
    private final EventMapper mapper;

    @PostMapping
    public ResponseEntity<EventFullDto> createEvent(@Valid @RequestBody NewEventDto dto,
                                                    @PathVariable Long userId) {

        Event event = service.createEvent(dto, userId);
        return new ResponseEntity<>(mapper.toFullDto(event), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<EventShortDto>> getUsers(@PathVariable Long userId,
                                                        @RequestParam(defaultValue = "0") Long from,
                                                        @RequestParam(defaultValue = "10") Long size) {

        List<EventShortDto> events = service.getEventsByUser(userId, from, size);
        log.info("events = {}", events);
        return new ResponseEntity<>(events, HttpStatus.OK);
    }
}
