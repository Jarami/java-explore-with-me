package ru.practicum.comment;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.NewCommentDto;
import ru.practicum.comment.dto.UpdateCommentDto;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/events/{eventId}/comments")
public class CommentPrivateController {

    private final CommentService service;
    private final CommentMapper mapper;

    @PostMapping
    public ResponseEntity<CommentDto> createComment(@PathVariable Long userId,
                                                    @PathVariable Long eventId,
                                                    @Valid @RequestBody NewCommentDto dto) {

        Comment comment = service.createComment(userId, eventId, dto);
        return new ResponseEntity<>(mapper.toDto(comment), HttpStatus.CREATED);
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable Long userId,
                                                    @PathVariable Long eventId,
                                                    @PathVariable Long commentId,
                                                    @Valid @RequestBody UpdateCommentDto dto) {

        Comment comment = service.updateComment(userId, commentId, dto);
        return new ResponseEntity<>(mapper.toDto(comment), HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long userId,
                                              @PathVariable Long eventId,
                                              @PathVariable Long commentId) {

        service.deleteByUser(userId, eventId, commentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
