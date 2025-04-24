package com.skthvl.cinemetrics.model.dto;

import java.math.BigInteger;
import lombok.Builder;

@Builder
public record AddRatingDto(int rating, BigInteger movieId, String userName, String comment) {}
