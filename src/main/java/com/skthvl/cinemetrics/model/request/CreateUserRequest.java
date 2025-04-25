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

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest {
  @NotBlank(message = "username must not be empty")
  private String username;

  @NotBlank(message = "password must not be empty")
  private String password;

  @NotEmpty(message = "roles must not be empty")
  @Size(min = 1)
  @AllowedRoles(
      allowed = {"USER", "ADMIN"},
      message = "Roles must be USER or ADMIN only")
  private List<String> roles;
}
