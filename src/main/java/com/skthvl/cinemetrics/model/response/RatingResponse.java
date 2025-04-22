package com.skthvl.cinemetrics.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RatingResponse {
  private int rating;
  private String movieTitle;
  private String userName;
  private String comment;
}
