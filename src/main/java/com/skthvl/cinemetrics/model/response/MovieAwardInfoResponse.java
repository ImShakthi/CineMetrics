package com.skthvl.cinemetrics.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Response class representing award-related information for a movie. This class is used to transfer
 * movie award data between the service layer and client.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MovieAwardInfoResponse {
  /** The year when the movie was released. */
  private int releaseYear;

  /** Indicates whether the movie has won any awards. */
  private boolean hasWon;
}
