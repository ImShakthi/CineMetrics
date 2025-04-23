package com.skthvl.cinemetrics.service;

import com.skthvl.cinemetrics.entity.UserAccount;
import com.skthvl.cinemetrics.exception.InvalidCredentialException;
import com.skthvl.cinemetrics.exception.UserDoesNotExistException;
import com.skthvl.cinemetrics.exception.UserNameAlreadyExistException;
import com.skthvl.cinemetrics.model.dto.UserDto;
import com.skthvl.cinemetrics.repository.UserAccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserAccountService {
  private final PasswordEncoder passwordEncoder;
  private final UserAccountRepository userAccountRepository;

  public UserAccountService(
      final PasswordEncoder passwordEncoder, final UserAccountRepository userAccountRepository) {
    this.passwordEncoder = passwordEncoder;
    this.userAccountRepository = userAccountRepository;
  }

  public void registerUser(final UserDto userDto) {
    if (userAccountRepository.existsByName(userDto.userName())) {
      log.error("User name already exists: {}", userDto.userName());
      throw new UserNameAlreadyExistException("user name already exists: " + userDto.userName());
    }

    final String passwordHash = passwordEncoder.encode(userDto.password());
    log.info("Password hash generated: {}", passwordHash);

    final UserAccount userAccount =
        UserAccount.builder().name(userDto.userName()).passwordHash(passwordHash).build();

    userAccountRepository.save(userAccount);
    log.info("User registered successfully: {}", userDto.userName());
  }

  public boolean isUserCredentialValid(final UserDto userDto) {
    final var userAccount =
        userAccountRepository
            .findByName(userDto.userName())
            .orElseThrow(UserDoesNotExistException::new);

    if (!passwordEncoder.matches(userDto.password(), userAccount.getPasswordHash())) {
      throw new InvalidCredentialException();
    }
    log.info("User credential validated successfully: {}", userDto.userName());
    return true;
  }

  public void deleteUser(final UserDto userDto) {
    final var userAccount =
        userAccountRepository
            .findByName(userDto.userName())
            .orElseThrow(UserDoesNotExistException::new);
    log.info("User account exists: {}", userDto.userName());

    if (!passwordEncoder.matches(userDto.password(), userAccount.getPasswordHash())) {
      log.error("invalid password for user: {}", userDto.userName());
      throw new InvalidCredentialException();
    }

    userAccountRepository.delete(userAccount);
    log.info("User account deleted successfully: {}", userDto.userName());
  }
}
