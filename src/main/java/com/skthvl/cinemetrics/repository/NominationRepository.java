package com.skthvl.cinemetrics.repository;

import com.skthvl.cinemetrics.entity.Nomination;
import com.skthvl.cinemetrics.model.dto.MovieAwardDto;
import java.math.BigInteger;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository interface for performing CRUD operations and custom queries on {@link Nomination}
 * entities.
 */
public interface NominationRepository extends JpaRepository<Nomination, BigInteger> {
  /**
   * Retrieves a list of award details for a specific movie title and category, ignoring case
   * sensitivity in the lookup. The award details include the release year and whether the movie has
   * won the award.
   *
   * @param title the title of the movie to search for
   * @param category the category of the award to search for
   * @return a list of {@link MovieAwardDto} containing the release year and award-winning
   *     information for the specified movie title and category
   */
  @Query(
      value =
          """
          SELECT new com.skthvl.cinemetrics.model.dto.MovieAwardDto(n.releaseYear, n.hasWon)
          FROM Nomination n
          JOIN n.movie m
          WHERE LOWER(m.title) = LOWER(:title) AND LOWER(n.category) = LOWER(:category)
          """)
  List<MovieAwardDto> findAwardDetailsByTitleIgnoreCase(
      @Param("title") final String title, @Param("category") final String category);
}
