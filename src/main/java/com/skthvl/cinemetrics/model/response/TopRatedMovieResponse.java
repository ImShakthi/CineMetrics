package com.skthvl.cinemetrics.model.response;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Response object representing a top-rated movie with its associated rating and box office
 * performance. This class is used to transfer movie data in API responses, particularly for
 * endpoints that return highly-rated movies.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TopRatedMovieResponse {
  /** The title of the movie. */
  private String title;

  /** The average rating of the movie, typically on a scale determined by the application. */
  private BigDecimal averageRating;

  /** The box office earnings of the movie in the base currency unit. */
  private Long boxOfficeValue;
}
