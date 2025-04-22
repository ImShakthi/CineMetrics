package com.skthvl.cinemetrics.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MovieAwardDto {
  private int releaseYear;
  private boolean hasWon;
}
