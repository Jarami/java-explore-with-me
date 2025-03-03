package ru.practicum.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateCommentDto {
    @NotBlank(message = "Текст комментария не должен быть пустым")
    @Size(max = 7000, message = "Текст комментария должен содержать до 7000 символов")
    private String text;
}
