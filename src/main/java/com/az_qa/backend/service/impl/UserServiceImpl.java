package com.az_qa.backend.service.impl;

import com.az_qa.backend.dto.request.UserRequest;
import com.az_qa.backend.dto.request.UserUpdateRequest;
import com.az_qa.backend.dto.response.UserResponse;
import com.az_qa.backend.entity.User;
import com.az_qa.backend.exception.ResourceNotFoundException;
import com.az_qa.backend.mapper.UserMapper;
import com.az_qa.backend.repository.UserRepository;
import com.az_qa.backend.service.UserService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  private final UserMapper userMapper;

  public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
    this.userRepository = userRepository;
    this.userMapper = userMapper;
  }

  @Override
  public UserResponse getUserById(Long id) {
    User user =
        userRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));

    return userMapper.toResponse(user);
  }

  @Override
  public List<UserResponse> getAllUsers() {
    List<User> users = userRepository.findAll();
    return userMapper.toResponseList(users);
  }

  @Override
  public UserResponse create(UserRequest request) {
    User saved = userRepository.save(userMapper.toEntity(request));
    return userMapper.toResponse(saved);
  }

  @Override
  public UserResponse update(Long id, UserUpdateRequest request) {
    User user =
        userRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));

    userMapper.updateFromRequest(request, user);

    User updatedUser = userRepository.save(user);
    return userMapper.toResponse(updatedUser);
  }
}
