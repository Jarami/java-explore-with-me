package ru.practicum.event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.user.User;

import java.util.List;

@Repository
public interface EventRepo extends JpaRepository<Event, Long> {

    String FIND_ALL_BY_USER_WITH_LIMIT_AND_OFFSET = """
            SELECT new ru.practicum.event.dto.EventShortDto(e.id,
                   e.title,
                   e.annotation,
                   new ru.practicum.category.dto.CategoryDto(e.category.id, e.category.name),
                   0,
                   e.eventDate,
                   new ru.practicum.user.dto.UserShortDto(e.initiator.id, e.initiator.name),
                   e.paid,
                   0)
            FROM Event e
            WHERE e.initiator = :user
            ORDER BY e.eventDate DESC
            LIMIT :size
            OFFSET :from
            """;

    @Query(FIND_ALL_BY_USER_WITH_LIMIT_AND_OFFSET)
    List<EventShortDto> findAllByUserWithLimitAndOffset(User user, long from, long size);
}
