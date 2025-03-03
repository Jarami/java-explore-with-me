package ru.practicum.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.dto.CommentDto;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/events/{eventId}/comments")
public class CommentPublicController {

    private final CommentService service;
    private final CommentMapper mapper;

    @GetMapping
    public ResponseEntity<List<CommentDto>> getComments(@PathVariable Long eventId,
                                                        @RequestParam(defaultValue = "0") long from,
                                                        @RequestParam(defaultValue = "10") long size) {

        List<Comment> comments = service.getEvents(eventId, from, size);
        return new ResponseEntity<>(mapper.toDto(comments), HttpStatus.OK);
    }
}
