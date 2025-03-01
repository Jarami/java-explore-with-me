package ru.practicum.event;

import jakarta.persistence.Embeddable;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
@Embeddable
public class Location {
    @Range(min = -90, max = 90)
    private Float lat;

    @Range(min = -180, max = 180)
    private Float lon;
}
