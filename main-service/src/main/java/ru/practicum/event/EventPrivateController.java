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
import ru.practicum.event.dto.UpdateEventUserRequest;
import ru.practicum.participation.Participation;
import ru.practicum.participation.ParticipationMapper;
import ru.practicum.participation.ParticipationService;
import ru.practicum.participation.dto.ParticipationRequestDto;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/events")
public class EventPrivateController {

    private final EventService service;
    private final EventMapper mapper;
    private final ParticipationService participationService;
    private final ParticipationMapper participationMapper;

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

        List<Event> events = service.getEventsByUser(userId, from, size);
        return new ResponseEntity<>(mapper.toShortDto(events), HttpStatus.OK);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventFullDto> getEvent(@PathVariable Long userId,
                                                 @PathVariable Long eventId) {

        Event event = service.getEvent(userId, eventId);
        return new ResponseEntity<>(mapper.toFullDto(event), HttpStatus.OK);
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> updateEvent(@PathVariable Long userId,
                                                    @PathVariable Long eventId,
                                                    @Valid @RequestBody UpdateEventUserRequest request) {

        Event event = service.updateEventByUser(userId, eventId, request);
        return new ResponseEntity<>(mapper.toFullDto(event), HttpStatus.OK);
    }

    @GetMapping("/{eventId}/requests")
    public ResponseEntity<List<ParticipationRequestDto>> getParticipationsOfEvent(@PathVariable Long userId,
                                                                                  @PathVariable Long eventId) {

        List<Participation> participations = participationService.getParticipationsOfEvent(userId, eventId);
        return new ResponseEntity<>(participationMapper.toDto(participations), HttpStatus.OK);

    }
}
