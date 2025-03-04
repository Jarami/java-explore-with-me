package ru.practicum.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.event.Event;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    String FIND_ALL_BE_EVENT = """
            SELECT c
            FROM Comment c
            WHERE c.event = :event
            ORDER BY c.createdOn DESC
            LIMIT :size
            OFFSET :from
            """;

    @Query(FIND_ALL_BE_EVENT)
    List<Comment> findAllByEvent(Event event, long from, long size);
}
