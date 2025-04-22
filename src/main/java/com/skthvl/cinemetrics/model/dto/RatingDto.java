package com.skthvl.cinemetrics.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RatingDto {
  private int rating;
  private String movieTitle;
  private String userName;
  private String comment;
}
