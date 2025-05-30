package com.skthvl.cinemetrics.util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A converter class for handling the persistence of a {@code List<String>} as a single {@code
 * String} in a database column.
 */
@Converter
public class StringListConverter implements AttributeConverter<List<String>, String> {

  private static final String DELIMITER = ",";

  @Override
  public String convertToDatabaseColumn(List<String> attribute) {
    return attribute != null ? String.join(DELIMITER, attribute) : "";
  }

  @Override
  public List<String> convertToEntityAttribute(String dbData) {
    return dbData != null && !dbData.isBlank()
        ? Arrays.asList(dbData.split(DELIMITER))
        : new ArrayList<>();
  }
}
