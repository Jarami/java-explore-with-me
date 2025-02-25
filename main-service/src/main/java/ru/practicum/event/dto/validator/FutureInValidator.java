package ru.practicum.event.dto.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;

public class FutureInValidator implements ConstraintValidator<FutureIn, LocalDateTime> {

    private long seconds;

    public void initialize(FutureIn constraintAnnotation) {
        this.seconds = constraintAnnotation.seconds();
    }

    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        LocalDateTime futureTime = LocalDateTime.now().plusSeconds(seconds);

        return !value.isBefore(futureTime);
    }
}