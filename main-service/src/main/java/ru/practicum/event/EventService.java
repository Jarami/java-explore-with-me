package ru.practicum.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.category.Category;
import ru.practicum.category.CategoryService;
import ru.practicum.event.dto.*;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.User;
import ru.practicum.user.UserService;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.event.EventState.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventService {

    private final UserService userService;
    private final CategoryService categoryService;
    private final EventRepo repo;
    private final EventMapper mapper;

    public Event createEvent(NewEventDto dto, long userId) {

        log.info("creating event from {} by user {}", dto, userId);

        User user = userService.getById(userId);
        Category category = categoryService.getById(dto.getCategory());

        Event event = mapper.toEvent(dto);
        event.setInitiator(user);
        event.setCategory(category);

        return repo.save(event);
    }

    public List<Event> getEventsByUser(long userId, long from, long size) {
        User user = userService.getById(userId);
        return repo.findAllByUserWithLimitAndOffset(user, from, size);
    }

    public List<Event> findEvents(List<Long> userIds, List<String> stateNames, List<Long> categoryIds,
                                         LocalDateTime rangeStart, LocalDateTime rangeEnd, Long from, Long size) {

        log.info("find events for userIds = {}, stateNames = {}, categoryIds = {}, rangeStart = {}, rangeEnd = {}, from = {}, size = {}",
                userIds, stateNames, categoryIds, rangeStart, rangeEnd, from, size);

        if (userIds != null && userIds.isEmpty()) {
            return List.of();
        }

        if (stateNames != null && stateNames.isEmpty()) {
            return List.of();
        }

        if (categoryIds != null && categoryIds.isEmpty()) {
            return List.of();
        }

        if (rangeStart != null && rangeEnd != null && rangeStart.isAfter(rangeEnd)) {
            throw new BadRequestException("Задан rangeEnd до rangeStart");
        }

        if (from < 0 || size <= 0) {
            return List.of();
        }

        List<User> users = getAllUsersById(userIds);
        List<Category> categories = getAllCategoriesById(categoryIds);
        List<EventState> states = getAllEventStateById(stateNames);

        return repo.findAllByParams(userIds == null, users,
                categoryIds == null, categories,
                stateNames == null, states,
                rangeStart == null, rangeStart,
                rangeEnd == null, rangeEnd,
                from, size);
    }

    public Event updateEventByAdmin(long eventId, UpdateEventRequest request) {
        Event event = getFullById(eventId);

        if (request.getStateAction() != null) {

            EventStateAction stateAction = EventStateAction.valueOf(request.getStateAction());
            EventState state = switch (stateAction) {
                case EventStateAction.PUBLISH_EVENT -> EventState.PUBLISHED;
                case EventStateAction.REJECT_EVENT -> EventState.CANCELED;
                default -> throw new BadRequestException("Запрещенный stateAction " + stateAction);
            };

            event.setState(state);
        }

        return updateEvent(event, request);
    }

    public Event updateEventByUser(long userId, long eventId, UpdateEventRequest request) {

        User user = userService.getById(userId);
        Event event = getFullById(eventId);

        log.info("update event {} by user {}", event, user);

        if (!event.getInitiator().equals(user)) {
            throw new NotFoundException("Событие с id = " + eventId + " не найдено.");
        }

        if (event.getState() == EventState.PUBLISHED) {
            throw new ConflictException("Нельзя изменить опубликованное событие");
        }

        if (request.getStateAction() != null) {

            EventStateAction stateAction = EventStateAction.valueOf(request.getStateAction());
            EventState state = switch (stateAction) {
                case EventStateAction.SEND_TO_REVIEW -> EventState.PENDING;
                case EventStateAction.CANCEL_REVIEW -> EventState.CANCELED;
                default -> throw new BadRequestException("Запрещенный stateAction " + stateAction);
            };

            event.setState(state);
        }

        return updateEvent(event, request);
    }

    private Event updateEvent(Event event, UpdateEventRequest request) {
        if (request.getTitle() != null) {
            event.setTitle(request.getTitle());
        }

        if (request.getAnnotation() != null) {
            event.setAnnotation(request.getAnnotation());
        }

        if (request.getCategory() != null) {
            Category category = categoryService.getById(request.getCategory());
            event.setCategory(category);
        }

        if (request.getDescription() != null) {
            event.setDescription(request.getDescription());
        }

        if (request.getEventDate() != null) {
            event.setEventDate(request.getEventDate());
        }

        if (request.getLocation() != null) {
            Location location = mapper.toLocation(request.getLocation());
            event.setLocation(location);
        }

        if (request.getPaid() != null) {
            event.setPaid(request.getPaid());
        }

        if (request.getParticipantLimit() != null) {
            event.setParticipantLimit(request.getParticipantLimit());
        }

        if (request.getRequestModeration() != null) {
            event.setRequestModeration(request.getRequestModeration());
        }

        return repo.save(event);
    }

    public Event getEvent(long userId, long eventId) {
        User user = userService.getById(userId);
        Event event = getFullById(eventId);

        if (event.getInitiator().equals(user)) {
            return event;
        } else {
            throw new NotFoundException("Событие с id = " + eventId + " не найдено.");
        }
    }

    public List<Event> searchEvents(String text, List<Long> categoryIds, Boolean paid, LocalDateTime rangeStart,
                                    LocalDateTime rangeEnd, boolean onlyAvailable, EventSearchSort sort, long from, long size) {

        log.info("search events for text {}, categoryIds {}, paid {}, rangeStart {}, rangeEnd {}, onlyAvailable {}, sort {}. from {}, size {}",
                text, categoryIds, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);

        if (categoryIds != null && categoryIds.isEmpty()) {
            return List.of();
        }

        if (rangeStart != null && rangeEnd != null && rangeStart.isAfter(rangeEnd)) {
            throw new BadRequestException("Задан rangeEnd до rangeStart");
        }

        if (rangeStart == null && rangeEnd == null) {
            rangeStart = LocalDateTime.now();
        }

        if (from < 0 || size <= 0) {
            return List.of();
        }

        String normalizedText = getNormalizedText(text);
        List<Category> categories = getAllCategoriesById(categoryIds);

        // Возвращаем весь подходящий набор событий без учета from и size,
        // потому что этот набор, возможно, придется сортировать по просмотрам.
        // А просмотры мы можем узнать из сервиса статистики после того, как
        // узнаем список событий-претендентов. Таким образом, забираем весь
        // набор событий-претендентов, узнаем из сервиса статистики кол-во
        // их просмотров, сортируем, берем нужную страницу.
        List<Event> events = repo.searchAllByParams(
                text == null, normalizedText,
                categoryIds == null, categories,
                paid == null, paid,
                rangeStart == null, rangeStart,
                rangeEnd == null, rangeEnd);

        if (onlyAvailable) {
            events = events.stream()
                    .filter(e -> e.getParticipantLimit() != 0 && e.getConfirmedRequests() < e.getParticipantLimit())
                    .collect(Collectors.toList());
        }

        // TODO: получить кол-во просмотров

        Comparator<Event> comp = switch (sort) {
            case EVENT_DATE -> Comparator.comparing(Event::getEventDate);
            case VIEWS -> Comparator.comparing(Event::getViews);
        };

        return events.stream()
                .sorted(comp)
                .skip(from)
                .limit(size)
                .toList();
    }

    public Event getPublishedEventById(long eventId) {
        Event event = getFullById(eventId);
        if (event.getState() != PUBLISHED) {
            throw new NotFoundException("Событие с id = " + eventId + " не найдено.");
        }

        return event;
    }

    private String getNormalizedText(String text) {
        return text == null ? null : text.toLowerCase().trim();
    }

    private List<User> getAllUsersById(List<Long> ids) {
        return ids == null ? List.of() : userService.getById(ids);
    }

    private List<Category> getAllCategoriesById(List<Long> ids) {
        return ids == null ? List.of() : categoryService.getById(ids);
    }

    private List<EventState> getAllEventStateById(List<String> stateNames) {
        return stateNames == null ? List.of() : EventState.getByNames(stateNames);
    }

    public Event getById(long eventId) {
        return repo.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие с id = " + eventId + " не найдено."));
    }

    public Event getFullById(long eventId) {
        // TODO: добавить views
        return repo.findFullById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие с id = " + eventId + " не найдено."));
    }

    public List<Event> getAllFullById(List<Long> eventIds) {
        // TODO: добавить views
        return repo.findAllFullById(eventIds);
    }
}
