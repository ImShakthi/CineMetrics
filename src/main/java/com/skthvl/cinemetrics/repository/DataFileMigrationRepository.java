package com.skthvl.cinemetrics.repository;

import com.skthvl.cinemetrics.entity.DataFileMigration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for performing CRUD operations and custom queries on {@link
 * DataFileMigration} entities.
 */
@Repository
public interface DataFileMigrationRepository extends JpaRepository<DataFileMigration, Integer> {

  /**
   * Checks whether a record exists in the database with the specified file path and file checksum.
   *
   * @param filePath the file path to search for
   * @param fileChecksum the checksum of the file to search for
   * @return true if a record with the specified file path and checksum exists, false otherwise
   */
  boolean existsByFilePathAndFileChecksum(final String filePath, final String fileChecksum);
}
