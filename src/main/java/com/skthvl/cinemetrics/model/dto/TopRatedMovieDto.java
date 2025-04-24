package com.skthvl.cinemetrics.model.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TopRatedMovieDto {
  private String title;

  private BigDecimal averageRating;

  private Long boxOfficeAmountUsd;
}
