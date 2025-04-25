package com.skthvl.cinemetrics.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AllowedRolesValidator implements ConstraintValidator<AllowedRoles, List<String>> {

  private Set<String> allowedRoles;

  @Override
  public void initialize(AllowedRoles constraintAnnotation) {
    allowedRoles = new HashSet<>(Arrays.asList(constraintAnnotation.allowed()));
  }

  @Override
  public boolean isValid(
      @NotNull final List<String> value, final ConstraintValidatorContext context) {
    return allowedRoles.containsAll(value);
  }
}
