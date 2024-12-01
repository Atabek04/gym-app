package com.epam.gym.validation.annotation;

import com.epam.gym.model.TrainingType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.stream.Collectors;

public class SpecializationValidator implements ConstraintValidator<ValidSpecialization, String> {
    private String acceptedValues;

    @Override
    public void initialize(ValidSpecialization constraintAnnotation) {
        acceptedValues = Arrays.stream(TrainingType.values())
                .map(TrainingType::name)
                .collect(Collectors.joining(", "));
    }

    @Override
    public boolean isValid(String specialization, ConstraintValidatorContext context) {
        boolean isValid = Arrays.stream(TrainingType.values())
                .anyMatch(type -> type.name().equalsIgnoreCase(specialization));

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Invalid specialization. Accepted values are: " + acceptedValues)
                    .addConstraintViolation();
        }

        return isValid;
    }
}
