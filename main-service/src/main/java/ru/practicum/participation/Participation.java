package ru.practicum.participation;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import ru.practicum.event.Event;
import ru.practicum.user.User;

import java.time.LocalDateTime;

import static ru.practicum.participation.ParticipationStatus.PENDING;

@Getter
@Setter
@Entity
@Builder
@Table(name = "participations")
@EqualsAndHashCode(of = {"id"})
@EntityListeners(AuditingEntityListener.class)
public class Participation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "requester_id")
    private User requester;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ParticipationStatus status;

    @CreatedDate
    @Column(name = "created_on", nullable = false)
    private LocalDateTime created;
}
