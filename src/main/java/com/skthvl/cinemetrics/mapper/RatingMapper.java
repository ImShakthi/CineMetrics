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

@Mapper(componentModel = "spring")
public interface RatingMapper {
  @Named("roundToTwoDecimal")
  static BigDecimal roundToTwoDecimal(final BigDecimal value) {
    return value == null ? null : value.setScale(2, RoundingMode.HALF_UP);
  }

  @Mapping(target = "rating", source = "rating")
  @Mapping(target = "movieTitle", source = "movieTitle")
  @Mapping(target = "userName", source = "userName")
  @Mapping(target = "comment", source = "comment")
  RatingResponse to(final RatingDto rating);

  List<RatingResponse> toRatingResponse(final List<RatingDto> rating);

  @Mapping(target = "title", source = "title")
  @Mapping(
      target = "averageRating",
      source = "averageRating",
      qualifiedByName = "roundToTwoDecimal")
  @Mapping(target = "boxOfficeValue", source = "boxOfficeAmountUsd")
  TopRatedMovieResponse to(final TopRatedMovieDto topRatedMovieDto);

  List<TopRatedMovieResponse> toTopRatedMovieResponse(
      final List<TopRatedMovieDto> topRatedMovieDtos);

  @Mapping(target = "rating", source = "rating")
  @Mapping(target = "movieTitle", source = "rating.movie.title")
  @Mapping(target = "userName", source = "rating.ratedBy.name")
  @Mapping(target = "comment", ignore = true)
  RatingDto toRatingDto(final Rating rating);
}
