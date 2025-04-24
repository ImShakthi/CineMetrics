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
import com.skthvl.cinemetrics.model.dto.AddRatingDto;
import com.skthvl.cinemetrics.model.dto.RatingDto;
import com.skthvl.cinemetrics.model.dto.TopRatedMovieDto;
import com.skthvl.cinemetrics.repository.MovieRepository;
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

@ExtendWith(MockitoExtension.class)
class RatingServiceTest {

  @Mock private RatingRepository ratingRepository;
  @Mock private UserAccountRepository userAccountRepository;
  @Mock private MovieRepository movieRepository;
  @Mock private OmdbApiClient omdbApiClient;

  private RatingService ratingService;

  @BeforeEach
  void setUp() {
    ratingService =
        new RatingService(ratingRepository, userAccountRepository, movieRepository, omdbApiClient);
  }

  @Test
  void getRatingInfo_shouldReturnRatingsForMovieTitle() {
    String title = "Inception";
    RatingDto ratingDto = new RatingDto(75, title, "test_user_name", "Great movie!");

    when(ratingRepository.findRatingDetailsByTitleIgnoreCase(title)).thenReturn(List.of(ratingDto));

    final List<RatingDto> result = ratingService.getRatingInfo(title);

    assertEquals(1, result.size());
    assertEquals("Inception", result.getFirst().getMovieTitle());
    verify(ratingRepository).findRatingDetailsByTitleIgnoreCase(title);
  }

  @Test
  void addRating_shouldSaveRatingSuccessfully() {
    MovieDetailsResponse omdbResponse = mock(MovieDetailsResponse.class);
    AddRatingDto addRatingDto = new AddRatingDto(85, 1L, "test_user_name", "Fantastic!");
    UserAccount user = new UserAccount();
    user.setName("test_user_name");

    Movie movie = new Movie();
    movie.setId(BigInteger.ONE);
    movie.setTitle("Inception");
    movie.setReleaseYear(2010);
    movie.setBoxOfficeAmountUsd(null); // simulate missing box office
    when(userAccountRepository.findByName("test_user_name")).thenReturn(Optional.of(user));
    when(movieRepository.findById(BigInteger.ONE)).thenReturn(Optional.of(movie));

    when(omdbApiClient.getMoveDetailsByTitleAndYear("Inception", 2010)).thenReturn(omdbResponse);
    when(omdbResponse.parseBoxOffice()).thenReturn(new BigInteger("825532764"));

    ratingService.addRating(addRatingDto);

    verify(ratingRepository).save(any(Rating.class));
    assertEquals(new BigInteger("825532764"), movie.getBoxOfficeAmountUsd());
  }

  @Test
  void addRating_shouldThrowUserDoesNotExistException_whenUserNotFound() {
    AddRatingDto addRatingDto = new AddRatingDto(80, 1L, "ghost", "Good");

    when(userAccountRepository.findByName("ghost")).thenReturn(Optional.empty());

    assertThrows(UserDoesNotExistException.class, () -> ratingService.addRating(addRatingDto));
  }

  @Test
  void addRating_shouldThrowMovieNotFoundException_whenMovieNotFound() {
    AddRatingDto addRatingDto = new AddRatingDto(42, 1L, "test_user_name", "Nice");
    UserAccount user = new UserAccount();
    when(userAccountRepository.findByName("test_user_name")).thenReturn(Optional.of(user));
    when(movieRepository.findById(BigInteger.ONE)).thenReturn(Optional.empty());

    assertThrows(MovieNotFoundException.class, () -> ratingService.addRating(addRatingDto));
  }

  @Test
  void addRating_shouldThrowDuplicateRatingException_onDataIntegrityViolation() {
    AddRatingDto addRatingDto = new AddRatingDto(53, 1L, "test_user_name", "Repeated");
    UserAccount user = new UserAccount();
    Movie movie = new Movie();
    movie.setTitle("Interstellar");
    movie.setBoxOfficeAmountUsd(BigInteger.TEN);

    when(userAccountRepository.findByName("test_user_name")).thenReturn(Optional.of(user));
    when(movieRepository.findById(BigInteger.ONE)).thenReturn(Optional.of(movie));
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
