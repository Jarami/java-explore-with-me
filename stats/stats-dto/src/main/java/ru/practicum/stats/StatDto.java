package ru.practicum.stats;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class StatDto {
    private String uri;

    private String app;

    private Long hits;
}
