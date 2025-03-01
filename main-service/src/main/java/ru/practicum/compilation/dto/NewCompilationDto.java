package ru.practicum.compilation.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.ArrayList;
import java.util.List;

@Data
public class NewCompilationDto {

    private List<Long> events = new ArrayList<>();

    private Boolean pinned = false;

    @NotBlank
    @Length(min = 1, max = 50, message = "Заголовок подборки должен содержать от 1-го до 50-ти символов")
    private String title;
}
