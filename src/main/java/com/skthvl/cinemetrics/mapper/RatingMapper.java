package com.skthvl.cinemetrics.mapper;

import com.skthvl.cinemetrics.entity.Rating;
import com.skthvl.cinemetrics.model.dto.RatingDto;
import com.skthvl.cinemetrics.model.dto.TopRatedMovieDto;
import com.skthvl.cinemetrics.model.response.RatingResponse;
import com.skthvl.cinemetrics.model.response.TopRatedMovieResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper interface for converting between rating-related data models, DTOs, and responses. Utilizes
 * MapStruct for generating implementations. This interface centralizes the mapping logic for
 * transforming `Rating`, `RatingDto`, `RatingResponse`, `TopRatedMovieDto`, and
 * `TopRatedMovieResponse` objects.
 *
 * <p>Key features include: - Conversion with custom mappings and transformations, such as rounding
 * decimal values. - Support for entity-to-DTO and DTO-to-response transformation.
 */
@Mapper(componentModel = "spring")
public interface RatingMapper {

  /**
   * Rounds a given {@code BigDecimal} value to two decimal places using the {@code
   * RoundingMode.HALF_UP} rounding mode.
   *
   * @param value the {@code BigDecimal} value to be rounded; can be null
   * @return the rounded {@code BigDecimal} value, or {@code null} if the input was null
   */
  @Named("roundToTwoDecimal")
  static BigDecimal roundToTwoDecimal(final BigDecimal value) {
    return value == null ? null : value.setScale(2, RoundingMode.HALF_UP);
  }

  /**
   * Converts a list of {@code RatingDto} objects into a list of {@code RatingResponse} objects.
   *
   * @param rating the list of {@code RatingDto} objects to be converted
   * @return the converted list of {@code RatingResponse} objects
   */
  List<RatingResponse> toRatingResponse(final List<RatingDto> rating);

  /**
   * Converts a {@code RatingDto} object into a {@code RatingResponse} object. The mapping
   * transforms rating details, including the numerical rating value, movie title, user name, and
   * optional comment, from the DTO to the response format.
   *
   * @param rating the {@code RatingDto} object containing movie rating details to be converted
   * @return the resulting {@code RatingResponse} object with mapped fields
   */
  RatingResponse to(final RatingDto rating);

  /**
   * Maps a {@code TopRatedMovieDto} object to a {@code TopRatedMovieResponse} object with specified
   * field mappings and transformations.
   *
   * @param topRatedMovieDto the {@code TopRatedMovieDto} object to be converted
   * @return the resulting {@code TopRatedMovieResponse} object
   */
  @Mapping(
      target = "averageRating",
      source = "averageRating",
      qualifiedByName = "roundToTwoDecimal")
  @Mapping(target = "boxOfficeValue", source = "boxOfficeAmountUsd")
  TopRatedMovieResponse to(final TopRatedMovieDto topRatedMovieDto);

  /**
   * Converts a list of {@code TopRatedMovieDto} objects into a list of {@code
   * TopRatedMovieResponse} objects.
   *
   * @param topRatedMovieDtos the list of {@code TopRatedMovieDto} objects to be converted
   * @return the converted list of {@code TopRatedMovieResponse} objects
   */
  List<TopRatedMovieResponse> toTopRatedMovieResponse(
      final List<TopRatedMovieDto> topRatedMovieDtos);

  /**
   * Converts a {@code Rating} entity to a {@code RatingDto} object with specific field mappings.
   * Maps the title of the movie and the name of the user from the associated entities, while
   * ignoring the "comment" field.
   *
   * @param rating the {@code Rating} entity to be converted
   * @return the resulting {@code RatingDto} object with mapped fields
   */
  @Mapping(target = "movieTitle", source = "rating.movie.title")
  @Mapping(target = "userName", source = "rating.ratedBy.name")
  @Mapping(target = "comment", ignore = true)
  RatingDto toRatingDto(final Rating rating);
}
