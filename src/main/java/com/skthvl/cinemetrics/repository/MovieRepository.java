package com.skthvl.cinemetrics.repository;

import com.skthvl.cinemetrics.entity.Movie;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
  List<Movie> findMovieByTitleAndReleaseYear(final String title, final int releasedYear);
}
