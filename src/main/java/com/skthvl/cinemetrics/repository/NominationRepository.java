package com.skthvl.cinemetrics.repository;

import com.skthvl.cinemetrics.entity.Nomination;
import com.skthvl.cinemetrics.model.dto.MovieAwardDto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NominationRepository extends JpaRepository<Nomination, Long> {
  @Query(value = """ 
          SELECT new com.skthvl.cinemetrics.model.dto.MovieAwardDto(n.releaseYear, n.hasWon)
          FROM Nomination n
          JOIN n.movie m
          WHERE LOWER(m.title) = LOWER(:title)
          """)
  List<MovieAwardDto> findAwardDetailsByTitleIgnoreCase(@Param("title") final String title);
}
