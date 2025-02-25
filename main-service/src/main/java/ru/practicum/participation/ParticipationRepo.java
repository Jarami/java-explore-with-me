package ru.practicum.participation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.event.Event;
import ru.practicum.user.User;

import java.util.List;

@Repository
public interface ParticipationRepo extends JpaRepository<Participation, Long> {
    List<Participation> findByRequester(User requester);

    List<Participation> findByEvent(Event event);
}
