package ru.practicum.participation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.participation.dto.ParticipationRequestDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/requests")
public class ParticipationController {

    private final ParticipationService service;
    private final ParticipationMapper mapper;

    @PostMapping
    public ResponseEntity<ParticipationRequestDto> createParticipation(@PathVariable Long userId,
                                                                       @RequestParam Long eventId) {

        Participation participation = service.createParticipation(userId, eventId);
        return new ResponseEntity<>(mapper.toDto(participation), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ParticipationRequestDto>> getUserParticipations(@PathVariable Long userId) {
        List<Participation> participations = service.getUserParticipations(userId);
        return new ResponseEntity<>(mapper.toDto(participations), HttpStatus.OK);
    }
}
