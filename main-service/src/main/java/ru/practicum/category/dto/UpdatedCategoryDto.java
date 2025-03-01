package ru.practicum.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdatedCategoryDto {
    @NotBlank(message = "имя не должно быть пустым")
    @Size(max = 50, message = "имя не должно быть длиннее 50 символов")
    private String name;
}
