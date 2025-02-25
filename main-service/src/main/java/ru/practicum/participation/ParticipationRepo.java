package ru.practicum.participation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipationRepo extends JpaRepository<Participation, Long> {
}
