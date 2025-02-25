package ru.practicum.event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.category.Category;
import ru.practicum.user.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepo extends JpaRepository<Event, Long> {

    String FIND_ALL_BY_USER_WITH_LIMIT_AND_OFFSET = """
            SELECT new Event(e, count(p.id), 0)
            FROM Event e
            LEFT JOIN Participation p ON p.event = event AND p.status = 'CONFIRMED'
            WHERE e.initiator = :user
            GROUP BY e
            ORDER BY e.eventDate DESC
            LIMIT :size
            OFFSET :from
            """;

    String FIND_ALL_BY_PARAMS = """
            SELECT new Event(e, count(p.id), 0)
            FROM Event e
            LEFT JOIN Participation p ON p.event = event AND p.status = 'CONFIRMED'
            WHERE ( :skipUsers = true OR e.initiator IN (:users) )
            AND ( :skipCategories = true OR e.category IN (:categories) )
            AND ( :skipStates = true OR e.state IN (:states) )
            AND ( :skipStart = true OR e.eventDate >= :start )
            AND ( :skipEnd = true OR e.eventDate <= :end )
            GROUP BY e
            ORDER BY e.eventDate DESC
            LIMIT :size
            OFFSET :from
            """;

    String FIND_FULL_BY_ID = """
            SELECT new Event(e, count(p.id), 0)
            FROM Event e
            LEFT JOIN Participation p ON p.event = event AND p.status = 'CONFIRMED'
            WHERE e.id = :eventId
            GROUP BY e
            """;

    @Query(FIND_ALL_BY_USER_WITH_LIMIT_AND_OFFSET)
    List<Event> findAllByUserWithLimitAndOffset(User user, long from, long size);

    @Query(FIND_ALL_BY_PARAMS)
    List<Event> findAllByParams(boolean skipUsers, List<User> users,
                                       boolean skipCategories, List<Category> categories,
                                       boolean skipStates, List<EventState> states,
                                       boolean skipStart, LocalDateTime start,
                                       boolean skipEnd, LocalDateTime end,
                                       long from, long size);

    @Query(FIND_FULL_BY_ID)
    Optional<Event> findFullById(long eventId);
}
