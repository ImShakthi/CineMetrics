package com.skthvl.cinemetrics.service;

import com.skthvl.cinemetrics.client.omdb.OmdbApiClient;
import com.skthvl.cinemetrics.entity.Rating;
import com.skthvl.cinemetrics.exception.DuplicateRatingException;
import com.skthvl.cinemetrics.exception.MovieNotFoundException;
import com.skthvl.cinemetrics.exception.UserDoesNotExistException;
import com.skthvl.cinemetrics.model.dto.AddRatingDto;
import com.skthvl.cinemetrics.model.dto.RatingDto;
import com.skthvl.cinemetrics.model.dto.TopRatedMovieDto;
import com.skthvl.cinemetrics.repository.MovieRepository;
import com.skthvl.cinemetrics.repository.RatingRepository;
import com.skthvl.cinemetrics.repository.UserAccountRepository;
import java.math.BigInteger;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class RatingService {
  private final RatingRepository ratingRepository;
  private final UserAccountRepository userAccountRepository;
  private final MovieRepository movieRepository;
  private final OmdbApiClient omdbApiClient;

  public RatingService(
      final RatingRepository ratingRepository,
      final UserAccountRepository userAccountRepository,
      final MovieRepository movieRepository,
      final OmdbApiClient omdbApiClient) {
    this.ratingRepository = ratingRepository;
    this.userAccountRepository = userAccountRepository;
    this.movieRepository = movieRepository;
    this.omdbApiClient = omdbApiClient;
  }

  @Transactional(readOnly = true)
  public List<RatingDto> getRatingInfo(final String title) {
    return ratingRepository.findRatingDetailsByTitleIgnoreCase(title);
  }

  @Transactional
  public void addRating(final AddRatingDto ratingDto) {
    final var userAccount =
        userAccountRepository
            .findByName(ratingDto.userName())
            .orElseThrow(UserDoesNotExistException::new);
    log.debug("User account exists: {}", ratingDto.userName());

    final var movie =
        movieRepository
            .findById(BigInteger.valueOf(ratingDto.movieId()))
            .orElseThrow(MovieNotFoundException::new);
    log.debug("Movie exists: {}", movie.getTitle());

    if (movie.isBoxOfficeEmpty()) {
      final var movieDetails =
          omdbApiClient.getMoveDetailsByTitleAndYear(movie.getTitle(), movie.getReleaseYear());

      movie.setBoxOfficeAmountUsd(movieDetails.parseBoxOffice());
      log.debug("Box office amount updated: {}", movie.getBoxOfficeAmountUsd());
    }
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
    } catch (DataIntegrityViolationException ex) {
      log.error("error adding rating {}", ex.getMessage());
      throw new DuplicateRatingException();
    }
  }

  @Transactional(readOnly = true)
  public List<TopRatedMovieDto> getTop10RatedMovies() {
    return ratingRepository.getTop10RatedMoviesOrderByBoxOffice();
  }
}
