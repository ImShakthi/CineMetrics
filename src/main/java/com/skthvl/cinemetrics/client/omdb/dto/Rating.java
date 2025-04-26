package com.skthvl.cinemetrics.client.omdb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a rating entry from an external source, typically used for movie ratings. Contains the
 * rating source (e.g., "IMDb", "Rotten Tomatoes") and its corresponding value.
 *
 * @param source The name of the rating provider
 * @param value The rating value in the provider's format
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Rating {
  /** The name or identifier of the rating source. */
  @JsonProperty("Source")
  private String source;

  /** The actual rating value as provided by the source. */
  @JsonProperty("Value")
  private String value;
}
