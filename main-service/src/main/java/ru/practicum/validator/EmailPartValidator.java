package ru.practicum.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmailPartValidator implements ConstraintValidator<EmailPart, String> {

    private int local;
    private int domain;

    public void initialize(EmailPart constraintAnnotation) {
        this.local = constraintAnnotation.local();
        this.domain = constraintAnnotation.domain();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (value == null) {
            return false;
        }

        if (!value.contains("@")) {
            return false;
        }

        String[] chunks = value.split("@");
        if (chunks.length != 2) {
            return false;
        }

        String localPart = chunks[0];
        String domainPart = chunks[1];

        int lastPointIndex = domainPart.lastIndexOf(".");
        if (lastPointIndex != -1) {
            domainPart = domainPart.substring(lastPointIndex);
        }

        return localPart.length() <= local && domainPart.length() <= domain;
    }
}
