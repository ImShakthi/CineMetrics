package com.skthvl.cinemetrics.model.dto;

import java.math.BigInteger;
import lombok.Builder;

/**
 * Data Transfer Object (DTO) for adding a new rating to a movie. This record encapsulates all
 * necessary information required to create a new movie rating.
 */
@Builder
public record AddRatingDto(int rating, BigInteger movieId, String userName, String comment) {}
