package com.skthvl.cinemetrics.model.response;

import java.math.BigInteger;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TopRatedMovieResponse {
  private String title;
  private double averageRating;
  private BigInteger boxOfficeValue;
}
