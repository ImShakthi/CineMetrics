package com.skthvl.cinemetrics.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.skthvl.cinemetrics.client.omdb.OmdbApiClient;
import com.skthvl.cinemetrics.client.omdb.dto.MovieDetailsResponse;
import com.skthvl.cinemetrics.entity.Movie;
import com.skthvl.cinemetrics.entity.Rating;
import com.skthvl.cinemetrics.entity.UserAccount;
import com.skthvl.cinemetrics.exception.DuplicateRatingException;
import com.skthvl.cinemetrics.exception.MovieNotFoundException;
import com.skthvl.cinemetrics.exception.UserDoesNotExistException;
import com.skthvl.cinemetrics.mapper.RatingMapper;
import com.skthvl.cinemetrics.model.dto.AddRatingDto;
import com.skthvl.cinemetrics.model.dto.RatingDto;
import com.skthvl.cinemetrics.model.dto.TopRatedMovieDto;
import com.skthvl.cinemetrics.repository.RatingRepository;
import com.skthvl.cinemetrics.repository.UserAccountRepository;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class RatingServiceTest {

  @Mock private RatingRepository ratingRepository;
  @Mock private UserAccountRepository userAccountRepository;
  @Mock private MovieService movieService;
  @Mock private OmdbApiClient omdbApiClient;
  @Mock private RatingMapper ratingMapper;

  private RatingService ratingService;

  @BeforeEach
  void setUp() {
    ratingService =
        new RatingService(ratingRepository, userAccountRepository, movieService, ratingMapper);
  }

  @Test
  void getRatingInfo_shouldReturnRatingsForMovieTitle() {
    final var pageable = Pageable.ofSize(5);
    String title = "Inception";
    RatingDto ratingDto = new RatingDto(75, title, "test_user_name", "Great movie!");

    when(ratingRepository.findRatingDetailsByTitleIgnoreCase(title, pageable))
        .thenReturn(new PageImpl<>(List.of(ratingDto)));

    final Page<RatingDto> result = ratingService.getRatingInfo(title, 0, 5);

    assertEquals(1, result.getTotalElements());
    assertEquals("Inception", result.getContent().getFirst().getMovieTitle());
    verify(ratingRepository).findRatingDetailsByTitleIgnoreCase(title, pageable);
  }

  @Test
  void addRating_shouldSaveRatingSuccessfully() {
    MovieDetailsResponse omdbResponse = mock(MovieDetailsResponse.class);
    AddRatingDto addRatingDto =
        new AddRatingDto(85, BigInteger.ONE, "test_user_name", "Fantastic!");
    UserAccount user = new UserAccount();
    user.setName("test_user_name");

    Movie movie = new Movie();
    movie.setId(BigInteger.ONE);
    movie.setTitle("Inception");
    movie.setReleaseYear(2010);
    movie.setBoxOfficeAmountUsd(BigInteger.valueOf(825532764));
    when(userAccountRepository.findByName("test_user_name")).thenReturn(Optional.of(user));
    when(movieService.getMovieById(BigInteger.ONE)).thenReturn(movie);

    ratingService.addRating(addRatingDto);

    verify(ratingRepository).save(any(Rating.class));
    assertEquals(new BigInteger("825532764"), movie.getBoxOfficeAmountUsd());
  }

  @Test
  void addRating_shouldThrowUserDoesNotExistException_whenUserNotFound() {
    AddRatingDto addRatingDto = new AddRatingDto(80, BigInteger.ONE, "ghost", "Good");

    when(userAccountRepository.findByName("ghost")).thenReturn(Optional.empty());

    assertThrows(UserDoesNotExistException.class, () -> ratingService.addRating(addRatingDto));
  }

  @Test
  void addRating_shouldThrowMovieNotFoundException_whenMovieNotFound() {
    AddRatingDto addRatingDto = new AddRatingDto(42, BigInteger.ONE, "test_user_name", "Nice");
    UserAccount user = new UserAccount();
    when(userAccountRepository.findByName("test_user_name")).thenReturn(Optional.of(user));
    when(movieService.getMovieById(BigInteger.ONE)).thenThrow(MovieNotFoundException.class);

    assertThrows(MovieNotFoundException.class, () -> ratingService.addRating(addRatingDto));
  }

  @Test
  void addRating_shouldThrowDuplicateRatingException_onDataIntegrityViolation() {
    AddRatingDto addRatingDto = new AddRatingDto(53, BigInteger.ONE, "test_user_name", "Repeated");
    UserAccount user = new UserAccount();
    Movie movie = new Movie();
    movie.setTitle("Interstellar");
    movie.setBoxOfficeAmountUsd(BigInteger.TEN);

    when(userAccountRepository.findByName("test_user_name")).thenReturn(Optional.of(user));
    when(movieService.getMovieById(BigInteger.ONE)).thenReturn(movie);
    when(ratingRepository.save(any())).thenThrow(DataIntegrityViolationException.class);

    assertThrows(DuplicateRatingException.class, () -> ratingService.addRating(addRatingDto));
  }

  @Test
  void shouldReturnTop10RatedMovies() {
    // Given
    List<TopRatedMovieDto> mockTop10 =
        List.of(
            new TopRatedMovieDto("Inception", BigDecimal.valueOf(95.9), 800000000L),
            new TopRatedMovieDto("Interstellar", BigDecimal.valueOf(94.8), 700000000L));

    when(ratingRepository.getTopRatedMoviesOrderByBoxOffice(10)).thenReturn(mockTop10);

    // When
    final List<TopRatedMovieDto> result = ratingService.getTopRatedMovies(10);

    // Then
    assertEquals(mockTop10, result);
    verify(ratingRepository).getTopRatedMoviesOrderByBoxOffice(10);
  }
}
