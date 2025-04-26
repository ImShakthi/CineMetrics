package com.skthvl.cinemetrics.service;

import static com.skthvl.cinemetrics.util.DateUtil.parseYearAndEdition;
import static com.skthvl.cinemetrics.util.FileUtil.calculateChecksum;
import static java.util.Objects.requireNonNull;

import com.skthvl.cinemetrics.entity.DataFileMigration;
import com.skthvl.cinemetrics.entity.Movie;
import com.skthvl.cinemetrics.entity.Nomination;
import com.skthvl.cinemetrics.repository.DataFileMigrationRepository;
import com.skthvl.cinemetrics.repository.MovieRepository;
import com.skthvl.cinemetrics.repository.NominationRepository;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service component responsible for loading Academy Award (Oscar) nomination data from a CSV file
 * into the application's database. This loader specifically handles Best Picture nominations and
 * ensures that data is only loaded once by tracking file checksums.
 *
 * <p>The loader processes CSV files containing Oscar nomination data, creates corresponding Movie
 * and Nomination entities, and persists them to the database. It includes mechanisms to prevent
 * duplicate data loading and handles data validation.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class AcademyAwardLoader {

  private static final String BEST_PICTURE = "BEST PICTURE";

  private final MovieRepository movieRepository;
  private final NominationRepository nominationRepository;
  private final DataFileMigrationRepository dataFileMigrationRepository;

  @Value("${cinemetrics.data.csv.academy-award-path}")
  private String csvPath;

  /**
   * Loads Oscar nomination data from a configured CSV file into the database. This method is
   * transactional and includes checksum validation to prevent duplicate data loading.
   *
   * <p>The method performs the following steps:
   *
   * <ul>
   *   <li>Checks if the data has already been migrated using file checksums
   *   <li>Reads and processes the CSV file, filtering for Best Picture nominations
   *   <li>Creates Movie and Nomination entities for each valid record
   *   <li>Saves the processed nominations to the database
   *   <li>Records the successful migration in the database
   * </ul>
   */
  @Transactional
  public void loadOscarNominations() {

    if (hasDataAlreadyMigrated()) {
      log.info("Academy Awards data has already been migrated");
      return;
    }
    log.info("MIGRATION needed :: Academy Awards data");

    try (var reader = getCsvReader()) {
      final List<Nomination> nominations =
          reader
              .lines()
              .skip(1) // Skip header
              .map(this::parseCsvLine)
              .filter(data -> BEST_PICTURE.equalsIgnoreCase(data[1].trim()))
              .map(this::createNomination)
              .filter(Objects::nonNull)
              .toList();

      nominationRepository.saveAll(nominations);
      log.info("Saved {} Oscar nominations for Best Picture", nominations.size());

      final DataFileMigration dataFileMigration =
          DataFileMigration.builder()
              .filePath(csvPath)
              .fileChecksum(calculateChecksum(csvPath, "MD5"))
              .build();

      dataFileMigrationRepository.save(dataFileMigration);
      log.info("Saved data file migration record for {}", dataFileMigration);
    } catch (Exception e) {
      log.error("Error loading Academy Awards data", e);
    }
  }

  /**
   * Checks if the Academy Awards data file has already been processed and loaded into the database.
   *
   * @return true if the file with the same checksum has already been processed, false otherwise
   */
  private boolean hasDataAlreadyMigrated() {
    final String checksum = calculateChecksum(csvPath, "MD5");
    log.info("checksum: {} for {}", checksum, csvPath);
    return dataFileMigrationRepository.existsByFilePathAndFileChecksum(csvPath, checksum);
  }

  /**
   * Creates a Nomination entity from an array of CSV data.
   *
   * @param data array containing nomination data in the order: year, category, nominee, additional
   *     info, win status
   * @return a new Nomination entity, or null if creation fails
   * @throws IllegalArgumentException if the year format is invalid
   */
  private Nomination createNomination(final String[] data) {
    final var yearInput = data[0];
    final var nominee = data[2].trim();
    final var additionalInfo = data[3].trim();
    final var hasWon = "YES".equalsIgnoreCase(data[4]);

    var yearAndEdition = parseYearAndEdition(yearInput);
    if (yearAndEdition.isEmpty()) {
      throw new IllegalArgumentException("Invalid year format: " + yearInput);
    }

    final int releaseYear = yearAndEdition.getFirst();
    final int edition = yearAndEdition.getLast();

    try {
      final Movie movie = findOrCreateMovie(nominee, releaseYear);

      return Nomination.builder()
          .movie(movie)
          .awardType("OSCAR")
          .category(BEST_PICTURE)
          .releaseYear(releaseYear)
          .edition(edition)
          .hasWon(hasWon)
          .nominee(nominee)
          .additionalInfo(additionalInfo)
          .build();
    } catch (Exception e) {
      log.error(
          "Error creating nomination for movie {} and year {} :: {}",
          nominee,
          releaseYear,
          e.getMessage());
      return null;
    }
  }

  /**
   * Creates a BufferedReader for reading the Academy Awards CSV file from the classpath.
   *
   * @return a BufferedReader instance for the CSV file
   * @throws NullPointerException if the CSV file cannot be found in the classpath
   */
  private BufferedReader getCsvReader() {
    log.debug("Loading Academy Awards CSV from {}", csvPath);
    return new BufferedReader(
        new InputStreamReader(
            requireNonNull(getClass().getClassLoader().getResourceAsStream(csvPath))));
  }

  /**
   * Finds an existing movie by title and year or creates a new one if it doesn't exist.
   *
   * @param title the movie title
   * @param year the movie release year
   * @return an existing or newly created Movie entity
   */
  private Movie findOrCreateMovie(final String title, final int year) {
    return movieRepository.findMovieByTitleAndReleaseYear(title, year).stream()
        .findFirst()
        .orElseGet(
            () ->
                movieRepository.save(
                    Movie.builder()
                        .title(title)
                        .releaseYear(year)
                        .boxOfficeAmountUsd(BigInteger.ZERO)
                        .build()));
  }

  /**
   * Parses a CSV line into an array of strings, correctly handling quoted values.
   *
   * @param line the CSV line to parse
   * @return an array of strings containing the parsed values
   */
  private String[] parseCsvLine(final String line) {
    return line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
  }
}
