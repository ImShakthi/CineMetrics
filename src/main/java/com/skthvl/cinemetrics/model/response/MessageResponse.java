package com.skthvl.cinemetrics.model.response;

import lombok.Builder;

/** A response record that encapsulates a simple message string. */
@Builder
public record MessageResponse(String message) {}
