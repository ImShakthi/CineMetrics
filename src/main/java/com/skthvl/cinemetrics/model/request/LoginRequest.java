package com.skthvl.cinemetrics.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequest {
  @NotBlank(message = "username must not be empty")
  private String username;

  @NotBlank(message = "password must not be empty")
  private String password;
}
