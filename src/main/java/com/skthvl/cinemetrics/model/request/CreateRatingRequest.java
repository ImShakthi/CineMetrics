package com.skthvl.cinemetrics.model.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request model for creating a new movie rating.
 * This class validates that the rating is between 1 and 100,
 * and ensures that both rating and comment fields are provided.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateRatingRequest {
  /**
   * The rating value for the movie.
   * Must be between 1 and 100 (inclusive).
   */
  @NotNull(message = "rating must not be empty")
  @Min(value = 1, message = "rating must be greater than or equal to 1")
  @Max(value = 100, message = "rating must be less than or equal to 100")
  private Integer rating;

  /**
   * User's comment about the movie.
   * Cannot be empty or blank.
   */
  @NotBlank(message = "comment must not be empty")
  private String comment;
}
