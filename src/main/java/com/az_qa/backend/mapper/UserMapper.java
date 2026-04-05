package com.az_qa.backend.mapper;

import com.az_qa.backend.dto.request.UserRequest;
import com.az_qa.backend.dto.request.UserUpdateRequest;
import com.az_qa.backend.dto.response.UserResponse;
import com.az_qa.backend.entity.User;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

  public UserMapper() {}

  public User toEntity(UserRequest request) {
    return new User(request.getName(), request.getEmail());
  }

  public UserResponse toResponse(User user) {
    return new UserResponse(user.getId(), user.getName(), user.getEmail());
  }

  public void updateFromRequest(UserUpdateRequest request, User user) {
    if (request.getName() != null) {
      user.setName(request.getName());
    }
    if (request.getEmail() != null) {
      user.setEmail(request.getEmail());
    }
  }

  public List<UserResponse> toResponseList(List<User> users) {
    return users.stream().map(this::toResponse).collect(Collectors.toList());
  }
}
