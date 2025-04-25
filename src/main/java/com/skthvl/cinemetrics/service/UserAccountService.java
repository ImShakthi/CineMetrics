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
import org.springframework.transaction.annotation.Transactional;

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

  @Transactional
  public void registerUser(final UserDto userDto) {
    if (userAccountRepository.existsByName(userDto.userName())) {
      log.warn("User name already exists: {}", userDto.userName());
      throw new UserNameAlreadyExistException(
          String.format("user name already exists: %s", userDto.userName()));
    }

    final UserAccount userAccount =
        UserAccount.builder()
            .name(userDto.userName())
            .passwordHash(passwordEncoder.encode(userDto.password()))
            .roles(userDto.roles())
            .build();

    userAccountRepository.save(userAccount);
    log.info("User registered successfully: {}", userDto.userName());
  }

  @Transactional(readOnly = true)
  public UserAccount getValidUserByCredential(final UserDto userDto) {
    return validateCredentials(userDto);
  }

  @Transactional
  public void deleteUser(final UserDto userDto) {
    final var userAccount = validateCredentials(userDto);

    userAccountRepository.delete(userAccount);
    log.info("User account deleted successfully: {}", userDto.userName());
  }

  private UserAccount validateCredentials(final UserDto userDto) {
    final var userAccount =
        userAccountRepository
            .findByName(userDto.userName())
            .orElseThrow(UserDoesNotExistException::new);

    if (!passwordEncoder.matches(userDto.password(), userAccount.getPasswordHash())) {
      throw new InvalidCredentialException();
    }
    log.info("User credential validated successfully: {}", userDto.userName());

    return userAccount;
  }
}
