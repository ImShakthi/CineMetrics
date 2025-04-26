package com.skthvl.cinemetrics.model.dto;

import java.math.BigInteger;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) representing movie information. This class encapsulates movie-related
 * data for transfer between different layers of the application.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieDto {
  /** Unique identifier for the movie. */
  private String movieId;

  /** Title of the movie. */
  private String title;

  /** Year when the movie was released. */
  private String releaseYear;

  /** Box office earnings in USD. */
  private BigInteger boxOfficeAmountUsd;

  /** Movie rating (e.g., PG, R, etc.). */
  private String rated;

  /** Release date of the movie. */
  private String released;

  /** Duration of the movie. */
  private String runtime;

  /** Movie genres. */
  private String genre;

  /** Movie director(s). */
  private String director;

  /** Movie writer(s). */
  private String writer;

  /** Main actors in the movie. */
  private String actors;

  /** Brief synopsis of the movie. */
  private String plot;

  /** Language(s) of the movie. */
  private String language;

  /** Country/countries of origin. */
  private String country;

  /** Awards received by the movie. */
  private String awards;

  /** URL to the movie poster. */
  private String poster;

  /** Metascore rating. */
  private String metascore;

  /** IMDB rating of the movie. */
  private String imdbRating;

  /** Number of IMDB votes. */
  private String imdbVotes;

  /** IMDB unique identifier. */
  private String imdbId;

  /** Type of media (e.g., movie, series). */
  private String type;

  /** DVD release date. */
  private String dvd;

  /** Production company. */
  private String production;

  /** Official website URL. */
  private String website;
}
