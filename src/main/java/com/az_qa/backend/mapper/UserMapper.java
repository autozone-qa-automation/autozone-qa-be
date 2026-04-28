package com.az_qa.backend.mapper;

import com.az_qa.backend.dto.request.UserRequest;
import com.az_qa.backend.dto.request.UserUpdateRequest;
import com.az_qa.backend.dto.response.UserResponse;
import com.az_qa.backend.entity.RoleEntity;
import com.az_qa.backend.entity.UserEntity;
import com.az_qa.backend.exception.ResourceNotFoundException;
import com.az_qa.backend.repository.RoleRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

  private final RoleRepository roleRepository;

  public UserMapper(RoleRepository roleRepository) {
    this.roleRepository = roleRepository;
  }

  public UserEntity toEntity(UserRequest request) {
    Boolean isActive = request.getIsActive() != null ? request.getIsActive() : true;
    RoleEntity role =
        roleRepository
            .findById(request.getRoleId())
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        "Role not found with ID: " + request.getRoleId()));

    return new UserEntity(
        request.getName(),
        request.getLastName(),
        request.getEmail(),
        request.getHashPassword(),
        isActive,
        role);
  }

  public UserResponse toResponse(UserEntity user) {
    return new UserResponse(
        user.getId(), user.getName(), user.getEmail(), user.getIsActive(), user.getRole());
  }

  public void updateFromRequest(UserUpdateRequest request, UserEntity user) {
    if (request.getName() != null) {
      user.setName(request.getName());
    }
    if (request.getLastName() != null) {
      user.setLastName(request.getLastName());
    }
    if (request.getEmail() != null) {
      user.setEmail(request.getEmail());
    }
    if (request.getHashPassword() != null) {
      user.setHashPassword(request.getHashPassword());
    }
    if (request.getIsActive() != null) {
      user.setIsActive(request.getIsActive());
    }
    if (request.getRoleId() != null) {
      RoleEntity role =
          roleRepository
              .findById(request.getRoleId())
              .orElseThrow(
                  () ->
                      new ResourceNotFoundException(
                          "Role not found with ID: " + request.getRoleId()));
      user.setRole(role);
    }
  }

  public List<UserResponse> toResponseList(List<UserEntity> users) {
    return users.stream().map(this::toResponse).collect(Collectors.toList());
  }
}
