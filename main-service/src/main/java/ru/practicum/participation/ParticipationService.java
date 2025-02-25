package ru.practicum.participation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.event.Event;
import ru.practicum.event.EventService;
import ru.practicum.event.EventState;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.User;
import ru.practicum.user.UserService;

import java.util.List;

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
        Event event = eventService.getFullById(eventId);

        log.info("event = {}", event);

        if (event.getInitiator().equals(user)) {
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
                .event(event)
                .requester(user)
                .status(status)
                .build();

        return repo.save(participation);
    }

    public List<Participation> getUserParticipations(long userId) {
        User user = userService.getById(userId);
        return repo.findByRequester(user);
    }

    public Participation getById(long id) {
        return repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Заявка на участие с id = " + id + " не найдена."));
    }
}
