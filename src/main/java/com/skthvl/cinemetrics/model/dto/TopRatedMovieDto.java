package com.skthvl.cinemetrics.model.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) representing a top-rated movie. This class encapsulates essential
 * information about highly rated movies, including their title, average rating, and box office
 * earnings.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TopRatedMovieDto {
  /** The title of the movie. */
  private String title;

  /** The average rating of the movie based on user ratings. */
  private BigDecimal averageRating;

  /** The box office earnings of the movie in USD. */
  private Long boxOfficeAmountUsd;
}
