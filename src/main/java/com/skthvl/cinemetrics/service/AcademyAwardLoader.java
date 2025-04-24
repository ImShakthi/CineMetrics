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

  private boolean hasDataAlreadyMigrated() {
    final String checksum = calculateChecksum(csvPath, "MD5");
    log.info("checksum: {} for {}", checksum, csvPath);
    return dataFileMigrationRepository.existsByFilePathAndFileChecksum(csvPath, checksum);
  }

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

  private BufferedReader getCsvReader() {
    log.debug("Loading Academy Awards CSV from {}", csvPath);
    return new BufferedReader(
        new InputStreamReader(
            requireNonNull(getClass().getClassLoader().getResourceAsStream(csvPath))));
  }

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

  private String[] parseCsvLine(final String line) {
    return line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
  }
}
