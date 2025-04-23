package com.skthvl.cinemetrics.repository;

import com.skthvl.cinemetrics.entity.Rating;
import com.skthvl.cinemetrics.model.dto.RatingDto;
import com.skthvl.cinemetrics.model.dto.TopRatedMovieDto;
import java.math.BigInteger;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends JpaRepository<Rating, BigInteger> {

  @Query(
      """
          SELECT new com.skthvl.cinemetrics.model.dto.RatingDto(r.rating, m.title, u.name, r.comment) FROM Rating r
          JOIN r.movie m
          JOIN r.ratedBy u
          WHERE LOWER(m.title) = LOWER(:title)
          """)
  List<RatingDto> findRatingDetailsByTitleIgnoreCase(final String title);

  @Query(
      value =
          """
          SELECT
              m.title AS title,
              AVG(r.rating) AS averageRating,
              m.box_office_amount_usd AS boxOfficeAmountUsd
          FROM rating r
          JOIN movie m ON m.id = r.movie_id
          GROUP BY m.id, m.title, m.box_office_amount_usd
          ORDER BY m.box_office_amount_usd DESC
          LIMIT 10
          """,
      nativeQuery = true)
  List<TopRatedMovieDto> getTop10RatedMoviesOrderByBoxOffice();
}
