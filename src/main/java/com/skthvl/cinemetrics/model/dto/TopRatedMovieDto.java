package com.skthvl.cinemetrics.model.dto;

import java.math.BigInteger;

public interface TopRatedMovieDto {
  String getTitle();

  double getAverageRating();

  BigInteger getBoxOfficeAmountUsd();
}
