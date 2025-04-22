package com.skthvl.cinemetrics.repository;

import com.skthvl.cinemetrics.entity.Rating;
import com.skthvl.cinemetrics.model.dto.RatingDto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RatingRepository extends JpaRepository<Rating, Long> {

  @Query("""
          SELECT new com.skthvl.cinemetrics.model.dto.RatingDto(r.rating, m.title, u.name, r.comment) FROM Rating r
          JOIN r.movie m
          JOIN r.ratedBy u
          WHERE LOWER(m.title) = LOWER(:title)
          """)
  List<RatingDto> findRatingDetailsByTitleIgnoreCase(final String title);
}
