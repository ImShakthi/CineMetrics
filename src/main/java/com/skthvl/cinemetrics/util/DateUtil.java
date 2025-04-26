package com.skthvl.cinemetrics.util;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Utility class for parsing date-related information.
 * This class provides methods to
 * - Extract year and edition numbers from formatted input strings.
 */
public class DateUtil {

  private static final Pattern SIMPLE_YEAR_PATTERN =
      Pattern.compile("(\\d{4})\\s*\\((\\d+)[a-z]{2}\\)");
  private static final Pattern SLASH_YEAR_PATTERN =
      Pattern.compile("(\\d{2})(\\d{2})/(\\d{2})\\s*\\((\\d+)[a-z]{2}\\)");

  /**
   * Parses a string to extract year and edition information based on predefined patterns.
   * If the input matches a specific pattern, this method extracts and returns the year and edition
   * as a list of integers. If no patterns match, an empty list is returned.
   *
   * @param input the input string potentially containing year and edition information
   * @return a list of integers where the first element is the year and the second is the edition,
   *         or an empty list if the input does not match any expected patterns
   */
  public static List<Integer> parseYearAndEdition(final String input) {
    var simpleMatcher = SIMPLE_YEAR_PATTERN.matcher(input);
    if (simpleMatcher.find()) {
      return List.of(
          Integer.parseInt(simpleMatcher.group(1)), Integer.parseInt(simpleMatcher.group(2)));
    }

    var slashMatcher = SLASH_YEAR_PATTERN.matcher(input);
    if (slashMatcher.find()) {
      int year = Integer.parseInt(slashMatcher.group(1) + slashMatcher.group(3));
      int edition = Integer.parseInt(slashMatcher.group(4));
      return List.of(year, edition);
    }

    return List.of();
  }
}
