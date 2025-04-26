package com.skthvl.cinemetrics.repository;

import com.skthvl.cinemetrics.entity.Movie;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for performing CRUD operations and custom queries on {@link Movie} entities.
 */
@Repository
public interface MovieRepository extends JpaRepository<Movie, BigInteger> {
  /**
   * Retrieves a list of movies with the specified title and release year.
   *
   * @param title the title of the movie to search for
   * @param releasedYear the release year of the movie to search for
   * @return a list of movies that match the given title and release year
   */
  List<Movie> findMovieByTitleAndReleaseYear(final String title, final int releasedYear);

  /**
   * Retrieves an optional movie based on the given title.
   *
   * @param title the title of the movie to find
   * @return an Optional containing the Movie if it exists, otherwise an empty Optional
   */
  Optional<Movie> findMovieByTitle(String title);
}
