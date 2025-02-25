package ru.practicum.participation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.event.Event;
import ru.practicum.event.EventService;
import ru.practicum.event.EventState;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.User;
import ru.practicum.user.UserService;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParticipationService {

    private final UserService userService;
    private final EventService eventService;
    private final ParticipationRepo repo;
    private final ParticipationMapper mapper;

    public Participation createParticipation(long userId, long eventId) {

        User user = userService.getById(userId);
        EventFullDto event = eventService.getFullById(eventId);

        log.info("event = {}", event);

        if (event.getInitiator().getId().equals(user.getId())) {
            throw new ConflictException("Нельзя отправлять заявку на участие в собственном событии");
        }

        if (event.getState() != EventState.PUBLISHED) {
            throw new ConflictException("Нельзя участвовать в неопубликованном событии");
        }

        if (event.getConfirmedRequests() >= event.getParticipantLimit()) {
            throw new ConflictException("Достигнут лимит участников события");
        }

        ParticipationStatus status = ParticipationStatus.PENDING;
        if (!event.getRequestModeration()) {
            status = ParticipationStatus.CONFIRMED;
        }

        Participation participation = Participation.builder()
                .event(eventService.getById(eventId))
                .requester(user)
                .status(status)
                .build();

        return repo.save(participation);
    }

    public Participation getById(long id) {
        return repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Заявка на участие с id = " + id + " не найдена."));
    }
}
