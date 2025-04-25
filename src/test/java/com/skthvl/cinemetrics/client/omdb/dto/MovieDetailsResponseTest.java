package com.skthvl.cinemetrics.client.omdb.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigInteger;
import org.junit.jupiter.api.Test;

class MovieDetailsResponseTest {
  @Test
  void testParseBoxOffice_withValidCurrencyString() {
    final var response = MovieDetailsResponse.builder().boxOffice("$123,456,789").build();

    assertEquals(BigInteger.valueOf(123456789L), response.parseBoxOffice());
  }

  @Test
  void testParseBoxOffice_withOnlyNumbers() {
    final var response = MovieDetailsResponse.builder().boxOffice("987654321").build();

    assertEquals(BigInteger.valueOf(987654321L), response.parseBoxOffice());
  }

  @Test
  void testParseBoxOffice_withInvalidCharacters() {
    final var response = MovieDetailsResponse.builder().boxOffice("abc$%1!2@3^").build();

    assertEquals(BigInteger.valueOf(123L), response.parseBoxOffice());
  }

  @Test
  void testParseBoxOffice_withEmptyString() {
    final var response = MovieDetailsResponse.builder().boxOffice("").build();

    assertThrows(NumberFormatException.class, response::parseBoxOffice);
  }

  @Test
  void testParseBoxOffice_withNoDigits() {
    final var response = MovieDetailsResponse.builder().boxOffice("ABCxyz").build();

    assertThrows(NumberFormatException.class, response::parseBoxOffice);
  }
}
