package com.skthvl.cinemetrics.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MovieAwardInfoResponse {
  private int releaseYear;
  private boolean hasWon;
}
