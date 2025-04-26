package com.skthvl.cinemetrics.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.math.BigInteger;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents a movie entity in the system. It captures details such as the movie's title, release
 * year, box office revenue, ratings, genres, and other metadata about the movie.
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Movie {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false, updatable = false)
  private BigInteger id;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private int releaseYear;

  @Column(nullable = false, columnDefinition = "BIGINT UNSIGNED")
  private BigInteger boxOfficeAmountUsd;

  @Column private String rated;

  @Column private String released;

  @Column private String runtime;

  @Column private String genre;

  @Column private String director;

  @Column private String writer;

  @Column private String actors;

  @Column private String plot;

  @Column private String language;

  @Column private String country;

  @Column private String awards;

  @Column private String poster;

  @Column private String metascore;

  @Column private String imdbRating;

  @Column private String imdbVotes;

  @Column private String imdbId;

  @Column private String type;

  @Column private String dvd;

  @Column private String production;

  @Column private String website;

  public boolean isBoxOfficeEmpty() {
    return boxOfficeAmountUsd == null || BigInteger.ZERO.equals(boxOfficeAmountUsd);
  }
}
