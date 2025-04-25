package com.skthvl.cinemetrics.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = AllowedRolesValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface AllowedRoles {
  String message() default "Invalid role(s) provided";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  String[] allowed(); // allowed values
}
