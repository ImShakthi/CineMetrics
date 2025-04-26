package com.skthvl.cinemetrics.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) representing a simplified view of a movie's award nomination. This
 * class contains information about the movie's release year and whether it has won an award.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MovieAwardDto {
  /** The year when the movie was released. */
  private int releaseYear;

  /** Indicates whether the movie has won the award. */
  private boolean hasWon;
}
