package com.skthvl.cinemetrics.model.request;

import com.skthvl.cinemetrics.validator.AllowedRoles;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request object for creating a new user in the system. This class contains all necessary
 * information required for user creation and includes validation rules for each field.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest {
  /** The username for the new user account. Must not be blank or empty. */
  @NotBlank(message = "username must not be empty")
  private String username;

  /** The password for the new user account. Must not be blank or empty. */
  @NotBlank(message = "password must not be empty")
  private String password;

  /**
   * List of roles assigned to the user. Must contain at least one role. Only "USER" and "ADMIN"
   * roles are allowed.
   */
  @NotEmpty(message = "roles must not be empty")
  @Size(min = 1)
  @AllowedRoles(
      allowed = {"USER", "ADMIN"},
      message = "Roles must be USER or ADMIN only")
  private List<String> roles;
}
