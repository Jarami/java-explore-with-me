package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.practicum.validator.FutureIn;

import java.time.LocalDateTime;

@Data
public class NewEventDto {
    @NotBlank(message = "Заголовок события должен присутствовать")
    @Size(min = 3, max = 120, message = "Заголовок события должен содержать от 20-ти до 2000 символов")
    private String title;

    @NotBlank(message = "Аннотация события должна присутствовать")
    @Size(min = 20, max = 2000, message = "Аннотация события должна содержать от 20-ти до 2000 символов")
    private String annotation;

    @NotNull(message = "Идентификатор события должен присутствовать")
    private Long category;

    @NotBlank(message = "Описание события должно присутствовать")
    @Size(min = 20, max = 7000, message = "Описание события должно содержать от 20-ти до 7000 символов")
    private String description;

    @FutureIn(seconds = 2*60*60)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    @NotNull
    private LocationDto location;

    private Boolean paid = false;

    @PositiveOrZero
    private Integer participantLimit = 0;

    private Boolean requestModeration = true;
}
