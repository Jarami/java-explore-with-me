package ru.practicum.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.practicum.user.dto.validator.EmailPart;

@Data
public class NewUserRequest {
    @NotBlank
    @Size(min = 2, max = 250, message = "Имя пользователя должно содержать от 2-х до 250-ти символов")
    private String name;

    @NotNull
    @EmailPart(local = 64, domain = 63, message = "Имя почтового ящика не должно превышать 64 символа, а имя домена - 63 символа")
    @Size(min = 6, max = 254, message = "Почта пользователя должна содержать от 6-ти до 254-х символов")
    private String email;
}
