package com.skthvl.cinemetrics.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateRatingRequest {
  @NotBlank(message = "rating must not be empty")
  private int rating;

  @NotNull(message = "movieId must not be empty")
  private Long movieId;

  @NotBlank(message = "comment must not be empty")
  private String comment;
}
