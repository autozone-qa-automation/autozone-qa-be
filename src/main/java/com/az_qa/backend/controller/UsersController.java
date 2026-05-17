/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.controller;

import com.az_qa.backend.service.UsersService;
import com.az_qa.backend.vo.UpdateUserVO;
import com.az_qa.backend.vo.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Users", description = "Endpoints for managing users")
public class UsersController {

  @Autowired UsersService usersService;

  /**
   * Retrieves all users.
   *
   * @return list of users
   */
  @GetMapping
  @Operation(
          summary = "Get all users",
          description = "Retrieves a list of all registered users.")
  @ApiResponses(
          value = {
                  @ApiResponse(
                          responseCode = "200",
                          description = "Users retrieved successfully",
                          content =
                          @Content(
                                  mediaType = "application/json",
                                  schema = @Schema(implementation = UserVO.class))),
                  @ApiResponse(
                          responseCode = "204",
                          description = "No users found",
                          content = @Content)
          })
  public ResponseEntity<List<UserVO>> getAllUsers() {
    return ResponseEntity.ok(usersService.getAllUsers());
  }

  /**
   * Creates a new user.
   *
   * @param userVO user payload to create
   * @return persisted user representation
   */
  @PostMapping
  @Operation(
      summary = "Create a new user",
      description = "Creates a new user with the provided information.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "201",
            description = "User created",
            content = @Content(schema = @Schema(implementation = UserVO.class))),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request payload",
            content =
                @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"email\":\"Email is mandatory\"}"))),
        @ApiResponse(
            responseCode = "409",
            description = "User already exists",
            content =
                @Content(
                    mediaType = "application/json",
                    examples =
                        @ExampleObject(
                            value =
                                "{\"timestamp\":\"2026-04-19T10:00:00\",\"message\":\"User with"
                                    + " email john.doe@example.com already exists\"}")))
      })
  public ResponseEntity<UserVO> addNew(@Valid @RequestBody UserVO userVO) {
    UserVO savedUser = usersService.add(userVO);
    if (savedUser == null) {
      return ResponseEntity.badRequest().build();
    }
    return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
  }

  /**
   * updates an existing user.
   *
   * @param userVO user payload to update
   * @return persisted user representation
   */
  @PutMapping("/{id}")
  @Operation(
      summary = "Update an existing user",
      description = "Updates an existing user with the provided information.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "User updated",
            content = @Content(schema = @Schema(implementation = UserVO.class))),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request payload",
            content =
                @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"email\":\"Email is mandatory\"}"))),
        @ApiResponse(
            responseCode = "404",
            description = "User not found",
            content = @Content(mediaType = "application/json")),
        @ApiResponse(
            responseCode = "409",
            description = "email already exists",
            content =
                @Content(
                    mediaType = "application/json",
                    examples =
                        @ExampleObject(
                            value =
                                "{\"timestamp\":\"2026-04-19T10:00:00\",\"message\":\"User with"
                                    + " email john.doe@example.com already exists\"}")))
      })
  public ResponseEntity<UserVO> updates(
      @PathVariable Long id, @Valid @RequestBody UpdateUserVO updateUserVO) {
    UserVO savedUser = usersService.update(id, updateUserVO);
    if (savedUser == null) {
      return ResponseEntity.badRequest().build();
    }
    return ResponseEntity.status(HttpStatus.OK).body(savedUser);
  }

  /**
   * Deactivates a user by id.
   *
   * @param id user id
   * @return no content response if deactivated, not found if user does not exist
   *
   */
  @PutMapping("/{id}/deactivate")
  public ResponseEntity<Void> deactivate(@PathVariable Long id) {
    usersService.deactivate(id);
    return ResponseEntity.noContent().build();
  }
}
