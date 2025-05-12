package com.skthvl.cinemetrics.repository;

import com.skthvl.cinemetrics.entity.Rating;
import com.skthvl.cinemetrics.model.dto.RatingDto;
import com.skthvl.cinemetrics.model.dto.TopRatedMovieDto;
import java.math.BigInteger;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for performing CRUD operations and custom queries on {@link Rating}
 * entities.
 */
@Repository
public interface RatingRepository extends JpaRepository<Rating, BigInteger> {

  /**
   * Retrieves a list of detailed rating information for a movie by its title, ignoring case
   * sensitivity. The details include the rating, movie title, user name, and comment.
   *
   * @param title the title of the movie to search for
   * @return a list of {@link RatingDto} objects containing the detailed rating information for the
   *     specified movie title
   */
  @Query(
      value =
          """
          SELECT
              new com.skthvl.cinemetrics.model.dto.RatingDto(r.rating, m.title, u.name, r.comment)
              FROM Rating r
          JOIN r.movie m
          JOIN r.ratedBy u
          WHERE LOWER(m.title) = LOWER(:title)
          """,
      countQuery =
          """
          SELECT
              COUNT(r)
              FROM Rating r
          JOIN r.movie m
          JOIN r.ratedBy u
          WHERE LOWER(m.title) = LOWER(:title)
          """)
  Page<RatingDto> findRatingDetailsByTitleIgnoreCase(final String title, final Pageable pageable);

  /**
   * Retrieves a list of the top-rated movies ordered by box office revenue in descending order, and
   * then by average rating in descending order. The query groups movies by their ID and calculates
   * the average rating for each movie.
   *
   * @param limit the maximum number of top-rated movies to return
   * @return a list of {@link TopRatedMovieDto}, each containing the title, average rating, and box
   *     office amount of the movie
   */
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
          ORDER BY  m.box_office_amount_usd DESC, averageRating DESC
          LIMIT :limit
          """,
      nativeQuery = true)
  List<TopRatedMovieDto> getTopRatedMoviesOrderByBoxOffice(@Param("limit") final int limit);
}
