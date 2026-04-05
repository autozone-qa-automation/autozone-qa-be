package com.az_qa.backend.service;

import com.az_qa.backend.dto.request.UserRequest;
import com.az_qa.backend.dto.request.UserUpdateRequest;
import com.az_qa.backend.dto.response.UserResponse;
import com.az_qa.backend.exception.ResourceNotFoundException;
import java.util.List;

/**
 * Service contract for user management operations.
 */
public interface UserService {

  /**
   * Retrieves a user by their ID.
   * @param id the user's ID
   * @return the matching user
   * @throws ResourceNotFoundException if no user exists with the given ID
   */
  UserResponse getUserById(Long id);

  /**
   * Retrieves all registered users.
   * @return a list of all users, or an empty list if none exist
   */
  List<UserResponse> getAllUsers();

  /**
   * Creates a new user.
   * @param request the data for the new user
   * @return the created user
   */
  UserResponse create(UserRequest request);

  /**
   * Updates an existing user.
   * @param id the ID of the user to update
   * @param request the new data to apply
   * @return the updated user
   * @throws ResourceNotFoundException if no user exists with the given ID
   */
  UserResponse update(Long id, UserUpdateRequest request);
}
