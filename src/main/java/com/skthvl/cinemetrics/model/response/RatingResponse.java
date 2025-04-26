package com.skthvl.cinemetrics.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Response object representing a movie rating with associated information. This class is used to
 * transfer rating data from the service layer to the client.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RatingResponse {
  /** The numerical rating value given to the movie. */
  private int rating;

  /** The title of the movie that was rated. */
  private String movieTitle;

  /** The name of the user who provided the rating. */
  private String userName;

  /** Optional comment or review text associated with the rating. */
  private String comment;
}
