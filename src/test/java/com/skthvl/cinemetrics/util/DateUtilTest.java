package com.skthvl.cinemetrics.util;

import static com.skthvl.cinemetrics.util.DateUtil.parseYearAndEdition;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;

class DateUtilTest {

  @Test
  void testParseYearAndEdition_withSimplePattern() {
    final List<Integer> actual = parseYearAndEdition("1994 (67th)");

    assertEquals(List.of(1994, 67), actual);
  }

  @Test
  void testParseYearAndEdition_withSlashPattern() {
    final List<Integer> actual = parseYearAndEdition("1932/33 (6th)");

    assertEquals(List.of(1933, 6), actual);
  }

  @Test
  void testParseYearAndEdition_invalidFormat() {
    final List<Integer> actual = parseYearAndEdition("Invalid Format");

    assertTrue(actual.isEmpty());
  }
}
