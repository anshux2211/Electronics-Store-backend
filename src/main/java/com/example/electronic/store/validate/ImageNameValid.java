package com.example.electronic.store.validate;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ImageValidator.class)
public @interface ImageNameValid {
    String message() default "Image Name is blank";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
