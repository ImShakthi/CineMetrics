package com.skthvl.cinemetrics.model.response;

import java.math.BigInteger;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MovieInfoResponse {
  private String movieId;
  private String title;
  private String releaseYear;
  private BigInteger boxOfficeAmountUsd;
  private String rated;
  private String released;
  private String runtime;
  private String genre;
  private String director;
  private String writer;
  private String actors;
  private String plot;
  private String language;
  private String country;
  private String awards;
  private String poster;
  private String metascore;
  private String imdbRating;
  private String imdbVotes;
  private String imdbID;
  private String type;
  private String dvd;
  private String production;
  private String website;
}
