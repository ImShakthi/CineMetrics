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
  private int rating;
  @NotNull private Long movieId;
  @NotBlank private String comment;
}
