package com.skthvl.cinemetrics.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom validation annotation to validate that the provided list of roles contains only the
 * allowed values. This constraint is used for ensuring compliance with a predefined set of role
 * values.
 */
@Documented
@Constraint(validatedBy = AllowedRolesValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface AllowedRoles {
  /**
   * Specifies the default error message to be used when the validation fails.
   *
   * @return the default error message when the provided roles are invalid
   */
  String message() default "Invalid role(s) provided";

  /**
   * Specifies the group or groups the constraint belongs to. Groups can be used to apply a subset
   * of constraints during validation based on the selected groups.
   *
   * @return the array of group classes that this constraint belongs to
   */
  Class<?>[] groups() default {};

  /**
   * Specifies a custom payload for clients of the {@code jakarta.validation} API. This can be used
   * by validation clients to assign custom metadata objects to a constraint.
   *
   * @return an array of {@code Payload} classes used to carry additional information about the
   *     violation
   */
  Class<? extends Payload>[] payload() default {};

  /**
   * Specifies the array of allowed role values for validation. This attribute defines the set of
   * valid values that are permitted for a field or method parameter annotated with
   * {@code @AllowedRoles}.
   *
   * @return an array of allowed role values
   */
  String[] allowed(); // allowed values
}
