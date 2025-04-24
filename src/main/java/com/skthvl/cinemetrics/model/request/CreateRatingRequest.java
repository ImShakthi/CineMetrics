package com.skthvl.cinemetrics.model.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
  @NotNull(message = "rating must not be empty")
  @Min(value = 1, message = "rating must be greater than or equal to 1")
  @Max(value = 100, message = "rating must be less than or equal to 100")
  private Integer rating;

  @NotBlank(message = "comment must not be empty")
  private String comment;
}
