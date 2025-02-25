package ru.practicum.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.category.Category;
import ru.practicum.category.CategoryService;
import ru.practicum.event.dto.*;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.User;
import ru.practicum.user.UserService;

import java.time.LocalDateTime;
import java.util.List;

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

    public List<EventShortDto> getEventsByUser(long userId, long from, long size) {
        User user = userService.getById(userId);
        return repo.findAllByUserWithLimitAndOffset(user, from, size);
    }

    public List<EventFullDto> findEvents(List<Long> userIds, List<String> stateNames, List<Long> categoryIds,
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
            return List.of();
        }

        List<User> users = getAllUsersById(userIds);
        List<Category> categories = getAllCategoriesById(categoryIds);
        List<EventState> states = getAllEventStateById(stateNames);

        return repo.findAllByParams(userIds == null, users,
                categoryIds == null, categories,
                stateNames == null, states,
                rangeStart == null, rangeStart == null ? LocalDateTime.now().minusDays(1) : rangeStart,
                rangeEnd == null, rangeEnd == null ? LocalDateTime.now().plusDays(1) : rangeEnd,
                from, size);
    }

    public EventFullDto updateEvent(long eventId, UpdateEventAdminRequest request) {
        Event event = getById(eventId);

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

        if (request.getStateAction() != null) {

            EventStateAction stateAction = EventStateAction.valueOf(request.getStateAction());
            EventState state = switch (stateAction) {
                case EventStateAction.PUBLISH_EVENT -> EventState.PUBLISHED;
                case EventStateAction.REJECT_EVENT -> EventState.CANCELED;
            };

            event.setState(state);
        }

        repo.save(event);

        return repo.findFullById(event.getId());
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

    private Event getById(long eventId) {
        return repo.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие с id = " + eventId + " не найдено."));
    }
}
