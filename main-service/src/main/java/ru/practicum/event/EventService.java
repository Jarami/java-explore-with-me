package ru.practicum.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import ru.practicum.category.Category;
import ru.practicum.category.CategoryService;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
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

    private List<User> getAllUsersById(List<Long> ids) {
        return ids == null ? List.of() : userService.getById(ids);
    }

    private List<Category> getAllCategoriesById(List<Long> ids) {
        return ids == null ? List.of() : categoryService.getById(ids);
    }

    private List<EventState> getAllEventStateById(List<String> stateNames) {
        return stateNames == null ? List.of() : EventState.getByNames(stateNames);
    }
}
