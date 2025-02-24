package ru.practicum.hit;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class HitDto {
    private Integer id;

    private String app;

    private String uri;

    private String ip;

    private String timestamp;
}
