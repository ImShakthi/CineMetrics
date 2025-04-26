package com.skthvl.cinemetrics.service;

import com.skthvl.cinemetrics.entity.Rating;
import com.skthvl.cinemetrics.exception.DuplicateRatingException;
import com.skthvl.cinemetrics.exception.UserDoesNotExistException;
import com.skthvl.cinemetrics.mapper.RatingMapper;
import com.skthvl.cinemetrics.model.dto.AddRatingDto;
import com.skthvl.cinemetrics.model.dto.RatingDto;
import com.skthvl.cinemetrics.model.dto.TopRatedMovieDto;
import com.skthvl.cinemetrics.repository.RatingRepository;
import com.skthvl.cinemetrics.repository.UserAccountRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for managing movie ratings, including creation, retrieval, and analytics.
 */
@Slf4j
@Service
public class RatingService {
  /** Repository for managing rating entities. */
  private final RatingRepository ratingRepository;
  /** Repository for managing user account entities. */
  private final UserAccountRepository userAccountRepository;
  /** Service for managing movie operations. */
  private final MovieService movieService;
  /** Mapper for converting between rating entities and DTOs. */
  private final RatingMapper ratingMapper;

  /**
   * Constructs a new RatingService with required dependencies.
   *
   * @param ratingRepository Repository for rating operations
   * @param userAccountRepository Repository for user account operations
   * @param movieService Service for movie operations
   * @param ratingMapper Mapper for rating entity conversions
   */
  public RatingService(
      final RatingRepository ratingRepository,
      final UserAccountRepository userAccountRepository,
      final MovieService movieService,
      final RatingMapper ratingMapper) {
    this.ratingRepository = ratingRepository;
    this.userAccountRepository = userAccountRepository;
    this.movieService = movieService;
    this.ratingMapper = ratingMapper;
  }

  /**
   * Retrieves all ratings for a movie by its title.
   *
   * @param title The movie title to search for
   * @return List of ratings for the specified movie
   */
  @Transactional(readOnly = true)
  public List<RatingDto> getRatingInfo(final String title) {
    return ratingRepository.findRatingDetailsByTitleIgnoreCase(title);
  }

  /**
   * Adds a new rating for a movie.
   *
   * @param ratingDto The rating information to add
   * @return The created rating
   * @throws UserDoesNotExistException if the user doesn't exist
   * @throws DuplicateRatingException if the user has already rated the movie
   */
  @Transactional
  public RatingDto addRating(final AddRatingDto ratingDto) {
    final var userAccount =
        userAccountRepository
            .findByName(ratingDto.userName())
            .orElseThrow(UserDoesNotExistException::new);
    log.debug("User account exists: {}", ratingDto.userName());

    final var movie = movieService.getMovieById(ratingDto.movieId());

    final var rating =
        Rating.builder()
            .rating(ratingDto.rating())
            .ratedBy(userAccount)
            .movie(movie)
            .comment(ratingDto.comment())
            .build();

    try {
      ratingRepository.save(rating);
      log.info("added rating for movie {} by user {}", movie.getTitle(), ratingDto.userName());

      return ratingMapper.toRatingDto(rating);
    } catch (DataIntegrityViolationException ex) {
      log.error("error adding rating {}", ex.getMessage());
      throw new DuplicateRatingException();
    }
  }

  /**
   * Retrieves top-rated movies ordered by box office earnings.
   *
   * @param limit Maximum number of movies to return
   * @return List of top-rated movies with their ratings and earnings
   */
  @Transactional(readOnly = true)
  public List<TopRatedMovieDto> getTopRatedMovies(final int limit) {
    return ratingRepository.getTopRatedMoviesOrderByBoxOffice(limit);
  }
}
