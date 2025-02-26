package ru.practicum.compilation.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
public class UpdateCompilationRequest {

    private List<Long> events;

    private Boolean pinned;

    @Length(min = 1, max = 50, message = "Заголовок подборки должен содержать от 1-го до 50-ти символов")
    private String title;
}
