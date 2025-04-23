package com.skthvl.cinemetrics.repository;

import com.skthvl.cinemetrics.entity.UserAccount;
import java.math.BigInteger;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, BigInteger> {
  boolean existsByName(String name);

  Optional<UserAccount> findByName(String name);
}
