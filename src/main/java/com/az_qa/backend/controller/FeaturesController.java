package com.az_qa.backend.controller;

import com.az_qa.backend.dto.request.UserRequest;
import com.az_qa.backend.dto.request.UserUpdateRequest;
import com.az_qa.backend.dto.response.UserResponse;
import com.az_qa.backend.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for user management endpoints.
 * Base path: /api/v1/users
 */
@RestController
@RequestMapping("/api/v1/users")
@Validated
public class FeaturesController {
  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserResponse> getUserById(@PathVariable @Positive long id) {
    return ResponseEntity.ok(userService.getUserById(id));
  }

  @GetMapping
  public ResponseEntity<List<UserResponse>> getAll() {
    return ResponseEntity.ok(userService.getAllUsers());
  }

  @PostMapping
  public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest request) {
    UserResponse createdUser = userService.create(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
  }

  @PutMapping("/{id}")
  public ResponseEntity<UserResponse> updateUser(
      @PathVariable @Positive Long id, @Valid @RequestBody UserUpdateRequest request) {
    return ResponseEntity.ok(userService.update(id, request));
  }
}
