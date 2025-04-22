package com.skthvl.cinemetrics.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class UserAccount extends Auditable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false, updatable = false)
  private BigInteger id;

  @Column(nullable = false)
  @NotBlank
  private String name;

  @Column(nullable = false)
  @NotBlank
  private String passwordHash;

  @OneToMany(mappedBy = "ratedBy", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Rating> ratings = new ArrayList<>();
}
