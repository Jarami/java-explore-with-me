package ru.practicum.participation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.event.Event;
import ru.practicum.user.User;

import java.util.List;

@Repository
public interface ParticipationRepo extends JpaRepository<Participation, Long> {
    List<Participation> findByRequester(User requester);

    List<Participation> findByEvent(Event event);

    List<Participation> findByEventAndStatus(Event event, ParticipationStatus status);

    @Modifying
    @Query("UPDATE Participation p SET p.status = :newStatus WHERE p.status = :oldStatus AND p.event = :event")
    int changeStatusForEventRequests(ParticipationStatus oldStatus, ParticipationStatus newStatus, Event event);
}
