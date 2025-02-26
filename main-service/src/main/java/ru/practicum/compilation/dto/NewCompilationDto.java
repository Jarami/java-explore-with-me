package ru.practicum.compilation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.ArrayList;
import java.util.List;

@Data
public class NewCompilationDto {

    private List<Long> events = new ArrayList<>();

    private Boolean pinned = false;

    @NotBlank
    @Length(max = 50, message = "Заголовок подборки не должен содержать более 50 символов")
    private String title;
}
