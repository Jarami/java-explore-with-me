package ru.practicum.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.category.Category;
import ru.practicum.category.CategoryService;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.user.User;
import ru.practicum.user.UserService;

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
}
