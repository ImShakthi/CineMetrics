package com.skthvl.cinemetrics.repository;

import com.skthvl.cinemetrics.entity.DataFileMigration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataFileMigrationRepository extends JpaRepository<DataFileMigration, Integer> {

  boolean existsByFilePathAndFileChecksum(final String filePath, final String fileChecksum);
}
