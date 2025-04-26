package com.skthvl.cinemetrics.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Validator class for the {@link AllowedRoles} annotation. This class ensures that a given list of
 * role strings contains only values specified as allowed in the {@code AllowedRoles} annotation.
 */
public class AllowedRolesValidator implements ConstraintValidator<AllowedRoles, List<String>> {

  private Set<String> allowedRoles;

  /**
   * Initializes the validator by extracting the allowed roles specified in the {@link AllowedRoles}
   * annotation.
   *
   * @param constraintAnnotation the annotation instance that contains the allowed role values
   */
  @Override
  public void initialize(AllowedRoles constraintAnnotation) {
    allowedRoles = new HashSet<>(Arrays.asList(constraintAnnotation.allowed()));
  }

  /**
   * Validates whether the given list of roles contains only the allowed values specified by the
   * {@link AllowedRoles} annotation.
   *
   * @param value the list of roles to validate; must not be null
   * @param context the context in which the constraint is evaluated
   * @return true if the list contains only allowed roles; false otherwise
   */
  @Override
  public boolean isValid(
      @NotNull final List<String> value, final ConstraintValidatorContext context) {
    return allowedRoles.containsAll(value);
  }
}
