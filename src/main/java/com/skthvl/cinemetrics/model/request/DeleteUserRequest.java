package com.skthvl.cinemetrics.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeleteUserRequest {
  @NotBlank(message = "username must not be empty")
  private String username;

  @NotBlank(message = "password must not be empty")
  private String password;
}
