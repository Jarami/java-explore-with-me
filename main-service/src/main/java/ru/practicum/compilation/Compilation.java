package ru.practicum.compilation;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import ru.practicum.event.Event;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "compilations")
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50, unique = true)
    @Length(min = 1, max = 50)
    private String title;

    @Column(nullable = false)
    private Boolean pinned;

    @OneToMany
    private List<Event> events = new ArrayList<>();
}
