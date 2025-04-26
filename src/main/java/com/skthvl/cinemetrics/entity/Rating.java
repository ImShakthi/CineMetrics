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

/**
 * Entity representing a user's rating and review for a movie. Extends Auditable to track creation
 * and modification timestamps.
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Rating extends Auditable {
  /** Unique identifier for the rating. */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false, updatable = false)
  private BigInteger id;

  /** The movie being rated. Lazy loaded to optimize performance. */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "movie_id")
  private Movie movie;

  /** The user who created this rating. Lazy loaded to optimize performance. */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_account_id")
  private UserAccount ratedBy;

  /** Numerical rating value given to the movie. Cannot be null. */
  @Column(nullable = false)
  private int rating;

  /** Optional review comment provided with the rating. */
  private String comment;
}
