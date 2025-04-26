package com.skthvl.cinemetrics.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.math.BigInteger;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents an award nomination related to a movie. This entity captures details about the
 * nomination, such as the associated movie, category, nominee, and whether it has won.
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Nomination extends Auditable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false, updatable = false)
  private BigInteger id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "movie_id")
  private Movie movie;

  @Column(nullable = false)
  private int releaseYear;

  @Column(nullable = false)
  private int edition;

  @Column(nullable = false)
  private String category;

  @Column(nullable = false)
  private String nominee;

  @Column(nullable = false)
  private boolean hasWon;

  @Column(nullable = false)
  private String additionalInfo;

  @Column(nullable = false)
  private String awardType;
}
