package ru.practicum.event;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import ru.practicum.category.Category;
import ru.practicum.exception.ConflictException;
import ru.practicum.user.User;

import java.time.LocalDateTime;
import static ru.practicum.event.EventState.*;

@Getter
@Setter
@Entity
@Table(name = "events")
@EntityListeners(AuditingEntityListener.class)
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Length(min = 3, max = 120)
    private String title;

    @Column(nullable = false)
    @Length(min = 20, max = 2000)
    private String annotation;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "initiator_id")
    private User initiator;

    @Column(nullable = false)
    @Length(min = 20, max = 7000)
    private String description;

    @Column(nullable = false)
    private LocalDateTime eventDate;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name = "lat", column = @Column(nullable = false, name = "location_lat")),
            @AttributeOverride(name = "lon", column = @Column(nullable = false, name = "location_lon"))
    })
    private Location location;

    @Column(nullable = false)
    private Boolean paid;

    @Column(nullable = false)
    @Range(min = 0)
    private Integer participantLimit;

    @Column(nullable = false)
    private Boolean requestModeration = true;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EventState state = PENDING;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdOn;

    private LocalDateTime publishedOn;

    public void setState(EventState newState) {
        if (newState == PUBLISHED && state != PENDING) {
            throw new ConflictException("Опубликовать можно только ожидающие события!");
        }
        if (newState == CANCELED && state == PUBLISHED){
            throw new ConflictException("Отклонить можно только неопубликованное событие!");
        }
        state = newState;
    }
}
