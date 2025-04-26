package com.skthvl.cinemetrics.client.omdb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigInteger;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Response object mapping JSON data from OMDB API movie details endpoint. Created automatically via
 * constructor, builder pattern, or JSON deserialization.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieDetailsResponse implements OmdbBaseResponse {

  @JsonProperty("Title")
  private String title;

  @JsonProperty("Year")
  private String year;

  @JsonProperty("Rated")
  private String rated;

  @JsonProperty("Released")
  private String released;

  @JsonProperty("Runtime")
  private String runtime;

  @JsonProperty("Genre")
  private String genre;

  @JsonProperty("Director")
  private String director;

  @JsonProperty("Writer")
  private String writer;

  @JsonProperty("Actors")
  private String actors;

  @JsonProperty("Plot")
  private String plot;

  @JsonProperty("Language")
  private String language;

  @JsonProperty("Country")
  private String country;

  @JsonProperty("Awards")
  private String awards;

  @JsonProperty("Poster")
  private String poster;

  @JsonProperty("Ratings")
  private List<Rating> ratings;

  @JsonProperty("Metascore")
  private String metascore;

  @JsonProperty("imdbRating")
  private String imdbRating;

  @JsonProperty("imdbVotes")
  private String imdbVotes;

  @JsonProperty("imdbID")
  private String imdbId;

  @JsonProperty("Type")
  private String type;

  @JsonProperty("DVD")
  private String dvd;

  @JsonProperty("BoxOffice")
  private String boxOffice;

  @JsonProperty("Production")
  private String production;

  @JsonProperty("Website")
  private String website;

  @JsonProperty("Response")
  private String response;

  @JsonProperty("Error")
  private String error;

  /**
   * Parses the box office string value into a BigInteger. Removes all non-digit characters before
   * parsing.
   *
   * @return box office value as BigInteger
   */
  public BigInteger parseBoxOffice() {
    return BigInteger.valueOf(Long.parseLong(boxOffice.replaceAll("[^\\d.]", "")));
  }
}
