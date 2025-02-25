package ru.practicum.event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.category.Category;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.user.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    String FIND_ALL_BY_PARAMS = """
            SELECT new ru.practicum.event.dto.EventFullDto(e.id,
                       e.title,
                       e.annotation,
                       new ru.practicum.category.dto.CategoryDto(e.category.id, e.category.name),
                       0,
                       e.createdOn,
                       e.publishedOn,
                       e.description,
                       e.eventDate,
                       new ru.practicum.user.dto.UserShortDto(e.initiator.id, e.initiator.name),
                       new ru.practicum.event.dto.LocationDto(e.location.lat, e.location.lon),
                       e.paid,
                       e.participantLimit,
                       e.requestModeration,
                       e.state,
                       0)
            FROM Event e
            WHERE ( :skipUsers = true OR e.initiator IN (:users) )
            AND ( :skipCategories = true OR e.category IN (:categories) )
            AND ( :skipStates = true OR e.state IN (:states) )
            AND ( :skipStart = true OR e.eventDate >= :start )
            AND ( :skipEnd = true OR e.eventDate <= :end )
            ORDER BY e.eventDate DESC
            LIMIT :size
            OFFSET :from
            """;

    String FIND_FULL_BY_ID = """
            SELECT new ru.practicum.event.dto.EventFullDto(e.id,
                       e.title,
                       e.annotation,
                       new ru.practicum.category.dto.CategoryDto(e.category.id, e.category.name),
                       0,
                       e.createdOn,
                       e.publishedOn,
                       e.description,
                       e.eventDate,
                       new ru.practicum.user.dto.UserShortDto(e.initiator.id, e.initiator.name),
                       new ru.practicum.event.dto.LocationDto(e.location.lat, e.location.lon),
                       e.paid,
                       e.participantLimit,
                       e.requestModeration,
                       e.state,
                       0)
            FROM Event e
            WHERE e.id = :eventId
            """;

    @Query(FIND_ALL_BY_USER_WITH_LIMIT_AND_OFFSET)
    List<EventShortDto> findAllByUserWithLimitAndOffset(User user, long from, long size);

    @Query(FIND_ALL_BY_PARAMS)
    List<EventFullDto> findAllByParams(boolean skipUsers, List<User> users,
                                       boolean skipCategories, List<Category> categories,
                                       boolean skipStates, List<EventState> states,
                                       boolean skipStart, LocalDateTime start,
                                       boolean skipEnd, LocalDateTime end,
                                       long from, long size);

    @Query(FIND_FULL_BY_ID)
    Optional<EventFullDto> findFullById(long eventId);
}
