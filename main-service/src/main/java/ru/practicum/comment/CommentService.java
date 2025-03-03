package ru.practicum.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;
import ru.practicum.comment.dto.NewCommentDto;
import ru.practicum.comment.dto.UpdateCommentDto;
import ru.practicum.event.Event;
import ru.practicum.event.EventService;
import ru.practicum.event.EventState;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.User;
import ru.practicum.user.UserService;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final UserService userService;
    private final EventService eventService;
    private final CommentRepository repo;
    private final CommentMapper mapper;

    public Comment createComment(long userId, long eventId, NewCommentDto dto) {

        log.info("save comment {}, userId = {}, eventId = {}", dto, userId, eventId);

        User author = userService.getById(userId);
        Event event = eventService.getById(eventId);

        if (event.getState() != EventState.PUBLISHED) {
            throw new NotFoundException("Событие с id = " + eventId + " не найдено");
        }

        Comment comment = mapper.toComment(dto);
        comment.setAuthor(author);
        comment.setEvent(event);
        comment.setText(excape(dto.getText()));

        log.info("save comment {}", comment);
        return repo.save(comment);
    }

    public List<Comment> getEvents(long eventId, long from, long size) {
        Event event = eventService.getById(eventId);

        if (from < 0 || size <= 0) {
            return List.of();
        }

        return repo.findAllByEvent(event, from, size);
    }

    public Comment updateComment(long userId, long commentId, UpdateCommentDto dto) {
        User user = userService.getById(userId);
        Comment comment = getById(commentId);

        if (!comment.getAuthor().equals(user)) {
            throw new NotFoundException("Комментарий с id = " + commentId + " не найден");
        }

        comment.setText(excape(dto.getText()));

        return repo.save(comment);
    }

    public void deleteByUser(long userId, long eventId, long commentId) {
        User user = userService.getById(userId);
        Event event = eventService.getById(eventId); // для проверки существования
        Comment comment = getById(commentId);

        if (!comment.getAuthor().equals(user)) {
            throw new NotFoundException("Комментарий с id = " + commentId + " не найден");
        }

        repo.delete(comment);
    }

    public void deleteByAdmin(long eventId, long commentId) {
        Event event = eventService.getById(eventId); // для проверки существования
        Comment comment = getById(commentId);

        repo.delete(comment);
    }

    public Comment getById(long commentId) {
        return repo.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Комментарий с id = " + commentId + " не найден"));
    }

    private String excape(String text) {
        return HtmlUtils.htmlEscape(text);
    }
}
