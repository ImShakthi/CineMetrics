package com.skthvl.cinemetrics.entity;

import jakarta.persistence.*;
import java.math.BigInteger;
import lombok.*;

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

  public boolean isBoxOfficeEmpty() {
    return boxOfficeAmountUsd == null || BigInteger.ZERO.equals(boxOfficeAmountUsd);
  }
}
