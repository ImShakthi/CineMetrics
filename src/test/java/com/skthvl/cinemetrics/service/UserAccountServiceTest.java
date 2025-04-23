package com.skthvl.cinemetrics.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.skthvl.cinemetrics.entity.UserAccount;
import com.skthvl.cinemetrics.exception.InvalidCredentialException;
import com.skthvl.cinemetrics.exception.UserDoesNotExistException;
import com.skthvl.cinemetrics.exception.UserNameAlreadyExistException;
import com.skthvl.cinemetrics.model.dto.UserDto;
import com.skthvl.cinemetrics.repository.UserAccountRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserAccountServiceTest {

  @Mock private PasswordEncoder passwordEncoder;
  @Mock private UserAccountRepository userAccountRepository;

  private UserAccountService userAccountService;

  @BeforeEach
  void setUp() {
    userAccountService = new UserAccountService(passwordEncoder, userAccountRepository);
  }

  @Test
  void registerUser_shouldSaveNewUser_whenUsernameDoesNotExist() {
    UserDto userDto = new UserDto("test_user_name", "secret");

    when(userAccountRepository.existsByName("test_user_name")).thenReturn(false);
    when(passwordEncoder.encode("secret")).thenReturn("hashed-secret");

    userAccountService.registerUser(userDto);

    ArgumentCaptor<UserAccount> captor = ArgumentCaptor.forClass(UserAccount.class);
    verify(userAccountRepository).save(captor.capture());
    assertEquals("test_user_name", captor.getValue().getName());
    assertEquals("hashed-secret", captor.getValue().getPasswordHash());
  }

  @Test
  void registerUser_shouldThrowException_whenUsernameExists() {
    UserDto userDto = new UserDto("test_user_name", "secret");

    when(userAccountRepository.existsByName("test_user_name")).thenReturn(true);

    UserNameAlreadyExistException ex =
        assertThrows(
            UserNameAlreadyExistException.class, () -> userAccountService.registerUser(userDto));

    assertEquals("user name already exists: test_user_name", ex.getMessage());
    verify(userAccountRepository, never()).save(any());
  }

  @Test
  void isUserCredentialValid_shouldReturnTrue_whenCredentialsMatch() {
    UserDto userDto = new UserDto("test_user_name", "secret");
    UserAccount user = new UserAccount();
    user.setName("test_user_name");
    user.setPasswordHash("hashed-secret");

    when(userAccountRepository.findByName("test_user_name")).thenReturn(Optional.of(user));
    when(passwordEncoder.matches("secret", "hashed-secret")).thenReturn(true);

    assertTrue(userAccountService.isUserCredentialValid(userDto));
  }

  @Test
  void isUserCredentialValid_shouldThrowException_whenUserNotFound() {
    when(userAccountRepository.findByName("ghost")).thenReturn(Optional.empty());

    assertThrows(
        UserDoesNotExistException.class,
        () -> userAccountService.isUserCredentialValid(new UserDto("ghost", "pass")));
  }

  @Test
  void isUserCredentialValid_shouldThrowException_whenPasswordMismatch() {
    UserAccount user = new UserAccount();
    user.setName("test_user_name");
    user.setPasswordHash("hashed");

    when(userAccountRepository.findByName("test_user_name")).thenReturn(Optional.of(user));
    when(passwordEncoder.matches("wrong", "hashed")).thenReturn(false);

    assertThrows(
        InvalidCredentialException.class,
        () -> userAccountService.isUserCredentialValid(new UserDto("test_user_name", "wrong")));
  }

  @Test
  void deleteUser_shouldDeleteUser_whenCredentialsMatch() {
    UserDto userDto = new UserDto("test_user_name", "secret");
    UserAccount user = new UserAccount();
    user.setName("test_user_name");
    user.setPasswordHash("hashed");

    when(userAccountRepository.findByName("test_user_name")).thenReturn(Optional.of(user));
    when(passwordEncoder.matches("secret", "hashed")).thenReturn(true);

    userAccountService.deleteUser(userDto);

    verify(userAccountRepository).delete(user);
  }

  @Test
  void deleteUser_shouldThrowException_whenUserNotFound() {
    when(userAccountRepository.findByName("ghost")).thenReturn(Optional.empty());

    assertThrows(
        UserDoesNotExistException.class,
        () -> userAccountService.deleteUser(new UserDto("ghost", "any")));
  }

  @Test
  void deleteUser_shouldThrowException_whenPasswordMismatch() {
    UserDto userDto = new UserDto("test_user_name", "wrong");
    UserAccount user = new UserAccount();
    user.setName("test_user_name");
    user.setPasswordHash("hashed");

    when(userAccountRepository.findByName("test_user_name")).thenReturn(Optional.of(user));
    when(passwordEncoder.matches("wrong", "hashed")).thenReturn(false);

    assertThrows(InvalidCredentialException.class, () -> userAccountService.deleteUser(userDto));

    verify(userAccountRepository, never()).delete(any());
  }
}
