package com.skthvl.cinemetrics.controller;

import static com.skthvl.cinemetrics.util.PageUtils.mapPaginatedResponse;

import com.skthvl.cinemetrics.mapper.RatingMapper;
import com.skthvl.cinemetrics.model.dto.AddRatingDto;
import com.skthvl.cinemetrics.model.request.CreateRatingRequest;
import com.skthvl.cinemetrics.model.response.MessageResponse;
import com.skthvl.cinemetrics.model.response.PaginatedResponse;
import com.skthvl.cinemetrics.model.response.RatingResponse;
import com.skthvl.cinemetrics.model.response.TopRatedMovieResponse;
import com.skthvl.cinemetrics.service.RatingService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.math.BigInteger;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * RatingController is a REST controller that provides endpoints to manage movie ratings. It handles
 * operations such as retrieving ratings for a specific movie, creating a new rating, and fetching
 * the top-rated movies.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1")
@Validated
public class RatingController {

  private final RatingService ratingService;
  private final RatingMapper ratingMapper;

  public RatingController(final RatingService ratingService, final RatingMapper ratingMapper) {
    this.ratingService = ratingService;
    this.ratingMapper = ratingMapper;
  }

  /**
   * Retrieves a list of movie ratings based on the specified movie title.
   *
   * @param title the title of the movie for which ratings are to be fetched. Must not be null or
   *     empty.
   * @param page The page index for pagination. Defaults to 0.
   * @param size The number of records per page for pagination. Defaults to 5.
   * @return a {@code ResponseEntity} containing a list of paged {@code RatingResponse} objects
   *     representing the ratings of the movie.
   */
  @GetMapping("/movies/{title}/ratings")
  public ResponseEntity<PaginatedResponse<RatingResponse>> getRatings(
      @PathVariable(name = "title") @NotBlank(message = "title must not be empty")
          final String title,
      @RequestParam(defaultValue = "0") final int page,
      @RequestParam(defaultValue = "5") final int size) {
    log.info("Getting ratings for movie: {}", title);
    final var ratingDtoPage = ratingService.getRatingInfo(title, page, size);

    return ResponseEntity.ok(mapPaginatedResponse(ratingDtoPage, ratingMapper::to));
  }

  /**
   * Creates a new rating for a specified movie.
   *
   * @param authentication the authentication object containing user credentials. Must not be null;
   *     authentication is required.
   * @param movieId the ID of the movie to which the rating will be associated. Must not be blank.
   * @param request the {@code CreateRatingRequest} object containing rating details such as rating
   *     value and comment. Must be valid and not null.
   * @return a {@code ResponseEntity} containing a {@code MessageResponse} that indicates the
   *     success or failure of adding the rating.
   */
  @SecurityRequirement(name = "bearerAuth")
  @PostMapping("/movies/{movieId}/ratings")
  public ResponseEntity<MessageResponse> createRating(
      final Authentication authentication,
      @PathVariable(name = "movieId") @NotBlank(message = "title must not be empty")
          final String movieId,
      @RequestBody @Valid final CreateRatingRequest request) {

    if (authentication == null) {
      return ResponseEntity.status(401).body(new MessageResponse("authentication is required"));
    }

    final var userName = authentication.getName();
    log.debug("User name: {}", userName);

    final AddRatingDto rating =
        AddRatingDto.builder()
            .movieId(new BigInteger(movieId))
            .rating(request.getRating())
            .comment(request.getComment())
            .userName(userName)
            .build();

    final var ratingDto = ratingService.addRating(rating);

    return ResponseEntity.ok(
        new MessageResponse("added rating to movie: " + ratingDto.getMovieTitle()));
  }

  /**
   * Retrieves a list of top-rated movies with a specified limit.
   *
   * @param limit the maximum number of top-rated movies to retrieve. Must be a positive integer.
   * @return a {@code ResponseEntity} containing a list of {@code TopRatedMovieResponse} objects
   *     that represent the top-rated movies.
   */
  @GetMapping("/ratings/top")
  public ResponseEntity<List<TopRatedMovieResponse>> getTopRatings(
      @RequestParam(name = "limit") final int limit) {
    log.info("Getting top 10 rated movies");
    final var topRatedMovies = ratingService.getTopRatedMovies(limit);
    log.info("Top 10 rated movies: {}", topRatedMovies);

    return ResponseEntity.ok(ratingMapper.toTopRatedMovieResponse(topRatedMovies));
  }
}
