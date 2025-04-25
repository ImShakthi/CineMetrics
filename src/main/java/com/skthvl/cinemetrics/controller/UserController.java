package com.skthvl.cinemetrics.controller;

import com.skthvl.cinemetrics.model.dto.UserDto;
import com.skthvl.cinemetrics.model.request.CreateUserRequest;
import com.skthvl.cinemetrics.model.request.DeleteUserRequest;
import com.skthvl.cinemetrics.model.response.MessageResponse;
import com.skthvl.cinemetrics.service.UserAccountService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@Validated
public class UserController {

  private final UserAccountService userAccountService;

  public UserController(final UserAccountService userAccountService) {
    this.userAccountService = userAccountService;
  }

  @PostMapping
  //  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ResponseEntity<MessageResponse> createUser(
      @RequestBody @Valid final CreateUserRequest request) {
    userAccountService.registerUser(
        new UserDto(request.getUsername(), request.getPassword(), List.of("USER")));

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new MessageResponse("user created successfully"));
  }

  @DeleteMapping
  public ResponseEntity<MessageResponse> deleteUser(
      @RequestBody @Valid final DeleteUserRequest request) {
    userAccountService.deleteUser(
        UserDto.builder().userName(request.getUsername()).password(request.getPassword()).build());

    return ResponseEntity.ok(new MessageResponse("user deleted successfully"));
  }
}
