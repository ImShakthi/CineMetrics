package com.skthvl.cinemetrics.entity;

import jakarta.persistence.*;
import java.math.BigInteger;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Nomination extends Auditable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false, updatable = false)
  private BigInteger id;

  @ManyToOne
  @JoinColumn(name = "movie_id")
  private Movie movie;

  @Column(nullable = false)
  private int year;

  @Column(nullable = false)
  private int edition;

  @Column(nullable = false)
  private String category;

  @Column(nullable = false)
  private String nomineeName;

  @Column(nullable = false)
  private boolean hasWon;

  @Column(nullable = false)
  private String additionalInfo;

  @Column(nullable = false)
  private String awardType;
}
