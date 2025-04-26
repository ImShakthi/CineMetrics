package com.skthvl.cinemetrics.model.response;

import java.math.BigInteger;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Response class representing detailed information about a movie. This class encapsulates various
 * attributes of a movie including basic information, production details, and rating metrics from
 * different sources.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MovieInfoResponse {
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

  /** Brief summary of the movie plot. */
  private String plot;

  /** Language(s) available for the movie. */
  private String language;

  /** Country of origin. */
  private String country;

  /** Awards received by the movie. */
  private String awards;

  /** URL to the movie poster image. */
  private String poster;

  /** Metascore rating. */
  private String metascore;

  /** IMDB rating score. */
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
