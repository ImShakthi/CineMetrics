package com.skthvl.cinemetrics.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) representing a movie rating in the system. This class is used to
 * transfer rating information between different layers of the application, particularly for API
 * responses and requests.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RatingDto {
  /** The numerical rating value given to the movie (typically on a scale of 1-100). */
  private int rating;

  /** The title of the movie being rated. */
  private String movieTitle;

  /** The username of the person who provided the rating. */
  private String userName;

  /** Optional comments or review text accompanying the rating. */
  private String comment;
}
