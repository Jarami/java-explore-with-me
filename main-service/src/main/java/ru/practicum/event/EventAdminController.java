package ru.practicum.event;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.UpdateEventAdminRequest;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
public class EventAdminController {
    private final EventService service;
    private final EventMapper mapper;

    @GetMapping
    public ResponseEntity<List<EventFullDto>> findEvents(@RequestParam(required = false) List<Long> users,
                                                         @RequestParam(required = false) List<String> states,
                                                         @RequestParam(required = false) List<Long> categories,
                                                         @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                                         @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                                         @RequestParam(defaultValue = "0") Long from,
                                                         @RequestParam(defaultValue = "10") Long size) {

        List<Event> events = service.findEvents(users, states, categories, rangeStart, rangeEnd, from, size);
        return new ResponseEntity<>(mapper.toFullDto(events), HttpStatus.OK);
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> updateEvent(@PathVariable Long eventId,
                                                    @Valid @RequestBody UpdateEventAdminRequest request) {

        Event event = service.updateEventByAdmin(eventId, request);
        return new ResponseEntity<>(mapper.toFullDto(event), HttpStatus.OK);
    }
}
