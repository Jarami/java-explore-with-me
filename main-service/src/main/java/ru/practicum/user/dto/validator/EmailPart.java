package ru.practicum.user.dto.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Email;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Email
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailPartValidator.class)
public @interface EmailPart {
    String message() default "Invalid date";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    int local() default 0;

    int domain() default 0;
}
