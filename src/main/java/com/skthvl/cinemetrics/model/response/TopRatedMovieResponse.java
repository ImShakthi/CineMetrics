package com.skthvl.cinemetrics.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TopRatedMovieResponse {
  private String title;
  private BigDecimal averageRating;
  private Long boxOfficeValue;
}
