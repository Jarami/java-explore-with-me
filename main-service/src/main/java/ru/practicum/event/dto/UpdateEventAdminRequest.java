package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.practicum.validator.FutureIn;

import java.time.LocalDateTime;

@Data
public class UpdateEventAdminRequest implements UpdateEventRequest {
    @Size(min = 3, max = 120, message = "Заголовок события должен содержать от 20-ти до 2000 символов")
    private String title;

    @Size(min = 20, max = 2000, message = "Аннотация события должна содержать от 20-ти до 2000 символов")
    private String annotation;

    private Long category;

    @Size(min = 20, max = 7000, message = "Описание события должно содержать от 20-ти до 7000 символов")
    private String description;

    @FutureIn(seconds = 60*60)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private LocationDto location;

    private Boolean paid;

    @PositiveOrZero
    private Integer participantLimit;

    private Boolean requestModeration;

    private String stateAction;
}
