package com.epam.gym.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SpecializationValidator.class)
public @interface ValidSpecialization {
    String message() default "Invalid specialization. Must be one of the valid training types.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}