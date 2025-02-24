package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NewEventDto {
    @NotBlank(message = "Заголовок события должен присутствовать")
    @Size(min = 3, max = 120, message = "Заголовок события должна содержать от 20-ти до 2000 символов")
    private String title;

    @NotBlank(message = "Аннотация события должен присутствовать")
    @Size(min = 20, max = 2000, message = "Аннотация события должна содержать от 20-ти до 2000 символов")
    private String annotation;

    @NotNull(message = "Идентификатор события должен присутствовать")
    private Long category;

    @NotBlank(message = "Описание события должен присутствовать")
    @Size(min = 20, max = 7000, message = "Описание события должно содержать от 20-ти до 7000 символов")
    private String description;

    @Future
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    @NotNull
    private LocationDto location;

    private Boolean paid;

    private Integer participantLimit = 0;

    private Boolean requestModeration = true;
}
