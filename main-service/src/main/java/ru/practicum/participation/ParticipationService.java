package ru.practicum.participation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.event.Event;
import ru.practicum.event.EventService;
import ru.practicum.event.EventState;
import ru.practicum.event.dto.EventRequestStatusUpdateRequest;
import ru.practicum.event.dto.EventRequestStatusUpdateResult;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.User;
import ru.practicum.user.UserService;

import java.util.List;

import static ru.practicum.participation.ParticipationStatus.*;

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

        if (event.getParticipantLimit() != 0 && event.getConfirmedRequests() >= event.getParticipantLimit()) {
            throw new ConflictException("Достигнут лимит участников события");
        }

        ParticipationStatus status = PENDING;
        if (!event.getRequestModeration()) {
            status = CONFIRMED;
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

    public Participation cancelParticipation(long userId, long requestId) {
        User user = userService.getById(userId);
        Participation participation = getById(requestId);

        if (!participation.getRequester().equals(user)) {
            throw new NotFoundException("Заявка с id = " + requestId + " не найдена");
        }

        participation.setStatus(REJECTED);

        return repo.save(participation);
    }

    public List<Participation> getParticipationsOfEvent(long userId, long eventId) {
        User user = userService.getById(userId);
        Event event = eventService.getById(eventId);

        if (!event.getInitiator().equals(user)) {
            throw new NotFoundException("Событие с id = " + eventId + " не найдено.");
        }

        return repo.findByEvent(event);
    }

    public EventRequestStatusUpdateResult updateParticipationStatus(long userId, long eventId, EventRequestStatusUpdateRequest request) {

        User user = userService.getById(userId);
        Event event = eventService.getFullById(eventId);

        if (!event.getInitiator().equals(user)) {
            throw new NotFoundException("Событие с id = " + eventId + " не найдено.");
        }

        if (event.getParticipantLimit() != 0 && event.getRequestModeration()) {
            List<Participation> participations = repo.findAllById(request.getRequestIds());

            if (request.getStatus() == CONFIRMED && event.getConfirmedRequests() + participations.size() > event.getParticipantLimit()) {
                throw new ConflictException("Достигнут лимит по заявкам на данное событие");
            }

            if (participations.stream().anyMatch(p -> p.getStatus() != PENDING)) {
                throw new ConflictException("Статус можно изменить только у заявок, находящихся в состоянии ожидания");
            }

            if (request.getStatus() == REJECTED) {
                participations.forEach(p -> p.setStatus(REJECTED));
                repo.saveAll(participations);

            } else {
                participations.stream()
                        .limit(event.getParticipantLimit() - event.getConfirmedRequests())
                        .forEach(p -> p.setStatus(CONFIRMED));

                repo.saveAll(participations);

                if (event.getConfirmedRequests() + participations.size() > event.getParticipantLimit()) {
                    repo.changeStatusForEventRequests(PENDING, REJECTED, event);
                }
            }
        }

        List<Participation> confirmedParticipations = repo.findByEventAndStatus(event, CONFIRMED);
        List<Participation> rejectedParticipations = repo.findByEventAndStatus(event, REJECTED);

        return new EventRequestStatusUpdateResult(
                mapper.toDto(confirmedParticipations),
                mapper.toDto(rejectedParticipations)
        );
    }

    public Participation getById(long id) {
        return repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Заявка на участие с id = " + id + " не найдена."));
    }
}
