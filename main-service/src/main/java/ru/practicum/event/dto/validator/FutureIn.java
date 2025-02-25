package ru.practicum.event.dto.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Email;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FutureInValidator.class)
public @interface FutureIn {
    String message() default "Invalid date";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    long seconds() default 0;
}
