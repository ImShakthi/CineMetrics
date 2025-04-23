package com.skthvl.cinemetrics.repository;

import com.skthvl.cinemetrics.entity.DataFileMigration;
import java.math.BigInteger;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataFileMigrationRepository extends CrudRepository<DataFileMigration, BigInteger> {
    List<DataFileMigration> findByName(String name);
}
