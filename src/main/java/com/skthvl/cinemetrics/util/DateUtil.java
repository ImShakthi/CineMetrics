package com.skthvl.cinemetrics.util;

import java.util.List;
import java.util.regex.Pattern;

public class DateUtil {

  private static final Pattern SIMPLE_YEAR_PATTERN =
      Pattern.compile("(\\d{4})\\s*\\((\\d+)[a-z]{2}\\)");
  private static final Pattern SLASH_YEAR_PATTERN =
      Pattern.compile("(\\d{2})(\\d{2})/(\\d{2})\\s*\\((\\d+)[a-z]{2}\\)");

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
